package com.rizhaosteel.droolstest.tagging.service;

import com.rizhaosteel.droolstest.tagging.model.TagRule;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 动态规则服务 - JDK 1.8兼容版本
 */
@Service
public class DynamicRuleService {
    
    @Autowired
    private RuleTemplateService ruleTemplateService;
    
    @Autowired
    private TagLibraryService tagLibraryService;
    
    // 规则存储
    private final Map<String, TagRule> ruleRepository = new ConcurrentHashMap<String, TagRule>();
    
    // KieContainer缓存
    private volatile KieContainer kieContainer;
    
    /**
     * 添加或更新规则
     */
    public boolean addOrUpdateRule(TagRule tagRule) {
        try {
            // 生成规则内容
            if (tagRule.getRuleContent() == null || tagRule.getRuleContent().trim().isEmpty()) {
                String generatedContent = ruleTemplateService.generateRuleContent(tagRule);
                tagRule.setRuleContent(generatedContent);
            }
            
            // 验证规则语法
            if (!ruleTemplateService.validateRule(tagRule.getRuleContent())) {
                System.err.println("规则语法验证失败: " + tagRule.getRuleCode());
                return false;
            }
            
            // 保存规则
            ruleRepository.put(tagRule.getRuleCode(), tagRule);
            
            // 重新编译规则引擎
            return recompileRules();
            
        } catch (Exception e) {
            System.err.println("添加/更新规则失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 删除规则
     */
    public boolean deleteRule(String ruleCode) {
        try {
            TagRule removedRule = ruleRepository.remove(ruleCode);
            if (removedRule != null) {
                return recompileRules();
            }
            return false;
        } catch (Exception e) {
            System.err.println("删除规则失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 启用/禁用规则
     */
    public boolean toggleRule(String ruleCode, boolean active) {
        TagRule rule = ruleRepository.get(ruleCode);
        if (rule != null) {
            rule.setActive(active);
            return recompileRules();
        }
        return false;
    }
    
    /**
     * 获取规则
     */
    public TagRule getRule(String ruleCode) {
        return ruleRepository.get(ruleCode);
    }
    
    /**
     * 获取所有规则
     */
    public List<TagRule> getAllRules() {
        return new ArrayList<TagRule>(ruleRepository.values());
    }
    
    /**
     * 根据标签获取规则
     */
    public List<TagRule> getRulesByTag(String tagCode) {
        List<TagRule> result = new ArrayList<TagRule>();
        for (TagRule rule : ruleRepository.values()) {
            if (tagCode.equals(rule.getTagCode())) {
                result.add(rule);
            }
        }
        return result;
    }
    
    /**
     * 重新编译规则引擎
     */
    private synchronized boolean recompileRules() {
        try {
            KieServices kieServices = KieServices.Factory.get();
            KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
            
            // 添加所有活跃的规则
            for (TagRule rule : ruleRepository.values()) {
                if (rule.isActive()) {
                    String resourcePath = "src/main/resources/rules/dynamic/" + rule.getRuleCode() + ".drl";
                    kieFileSystem.write(resourcePath, rule.getRuleContent());
                }
            }
            
            // 编译规则
            KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
            kieBuilder.buildAll();
            
            Results results = kieBuilder.getResults();
            if (results.hasMessages(Message.Level.ERROR)) {
                System.err.println("规则编译错误:");
                for (Message message : results.getMessages(Message.Level.ERROR)) {
                    System.err.println(message.getText());
                }
                return false;
            }
            
            // 更新KieContainer
            this.kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
            
            System.out.println("规则引擎重新编译成功，共加载 " + ruleRepository.size() + " 条规则");
            return true;
            
        } catch (Exception e) {
            System.err.println("重新编译规则引擎失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 创建新的KieSession
     */
    public KieSession createKieSession() {
        if (kieContainer == null) {
            recompileRules();
        }
        
        if (kieContainer != null) {
            KieSession session = kieContainer.newKieSession();
            session.setGlobal("tagLibraryService", tagLibraryService);
            return session;
        }
        
        return null;
    }
    
    /**
     * 测试规则
     */
    public Map<String, Object> testRule(String ruleCode, Object testData) {
        Map<String, Object> result = new HashMap<String, Object>();
        
        try {
            TagRule rule = ruleRepository.get(ruleCode);
            if (rule == null) {
                result.put("success", false);
                result.put("message", "规则不存在");
                return result;
            }
            
            KieSession session = createKieSession();
            if (session == null) {
                result.put("success", false);
                result.put("message", "创建规则会话失败");
                return result;
            }
            
            // 插入测试数据
            session.insert(testData);
            
            // 执行规则
            int firedRules = session.fireAllRules();
            
            result.put("success", true);
            result.put("firedRules", firedRules);
            result.put("message", "规则测试完成，触发了 " + firedRules + " 条规则");
            
            // 更新执行统计
            rule.incrementExecutionCount();
            
            session.dispose();
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "规则测试失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 获取规则统计信息
     */
    public Map<String, Object> getRuleStatistics() {
        Map<String, Object> stats = new HashMap<String, Object>();
        
        long totalRules = ruleRepository.size();
        long activeRules = 0;
        for (TagRule rule : ruleRepository.values()) {
            if (rule.isActive()) {
                activeRules++;
            }
        }
        long inactiveRules = totalRules - activeRules;
        
        stats.put("totalRules", totalRules);
        stats.put("activeRules", activeRules);
        stats.put("inactiveRules", inactiveRules);
        
        // 按模板分组统计
        Map<String, Long> templateStats = new HashMap<String, Long>();
        for (TagRule rule : ruleRepository.values()) {
            String template = rule.getRuleTemplate();
            templateStats.put(template, templateStats.getOrDefault(template, 0L) + 1);
        }
        stats.put("templateStats", templateStats);
        
        // 执行次数统计
        long totalExecutions = 0;
        for (TagRule rule : ruleRepository.values()) {
            totalExecutions += rule.getExecutionCount();
        }
        stats.put("totalExecutions", totalExecutions);
        
        return stats;
    }
    
    /**
     * 初始化默认规则
     */
    public void initializeDefaultRules() {
        // 技术专家规则
        TagRule techExpertRule = new TagRule("RULE_TECH_EXPERT", "技术专家规则", "TECH_EXPERT", "COMPOSITE");
        techExpertRule.setDescription("基于工作经验、技能数量和技能等级判断技术专家");
        techExpertRule.setConditions("{\"minYears\": 8, \"minSkills\": 5, \"avgSkillLevel\": 4}");
        techExpertRule.setPriority(9);
        addOrUpdateRule(techExpertRule);
        
        // 高绩效规则
        TagRule highPerformerRule = new TagRule("RULE_HIGH_PERFORMER", "高绩效规则", "HIGH_PERFORMER", "PERFORMANCE_BASED");
        highPerformerRule.setDescription("基于绩效评分判断高绩效员工");
        highPerformerRule.setConditions("{\"minScore\": 85, \"operator\": \">=\"}");
        highPerformerRule.setPriority(8);
        addOrUpdateRule(highPerformerRule);
        
        // 博士学历规则
        TagRule phdRule = new TagRule("RULE_PHD_DEGREE", "博士学历规则", "PHD_DEGREE", "EDUCATION_BASED");
        phdRule.setDescription("基于学历判断博士学位");
        phdRule.setConditions("{\"educationLevels\": [\"博士\"], \"matchType\": \"ANY\"}");
        phdRule.setPriority(7);
        addOrUpdateRule(phdRule);
        
        // 强领导力规则
        TagRule leadershipRule = new TagRule("RULE_STRONG_LEADER", "强领导力规则", "STRONG_LEADER", "LEADERSHIP_BASED");
        leadershipRule.setDescription("基于领导力评分、管理经验和团队规模判断强领导力");
        leadershipRule.setConditions("{\"minLeadershipScore\": 85, \"minManagementYears\": 3, \"minTeamSize\": 5}");
        leadershipRule.setPriority(9);
        addOrUpdateRule(leadershipRule);
        
        // 全栈开发者规则
        TagRule fullstackRule = new TagRule("RULE_FULLSTACK_DEV", "全栈开发者规则", "FULLSTACK_DEV", "SKILL_BASED");
        fullstackRule.setDescription("基于前后端技能判断全栈开发者");
        fullstackRule.setConditions("{\"requiredSkills\": [\"Java\", \"JavaScript\", \"数据库\"], \"matchType\": \"ALL\"}");
        fullstackRule.setPriority(8);
        addOrUpdateRule(fullstackRule);
        
        System.out.println("默认规则初始化完成");
    }
}
