package com.rizhaosteel.droolstest.tagging.service;

import com.rizhaosteel.droolstest.tagging.model.RuleTemplate;
import com.rizhaosteel.droolstest.tagging.model.TagRule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 规则模板服务 - JDK 1.8兼容版本
 */
@Service
public class RuleTemplateService {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 根据模板生成规则内容
     */
    public String generateRuleContent(TagRule tagRule) {
        RuleTemplate template = RuleTemplate.valueOf(tagRule.getRuleTemplate());
        Map<String, Object> conditions = parseConditions(tagRule.getConditions());
        
        switch (template) {
            case EXPERIENCE_BASED:
                return generateExperienceBasedRule(tagRule, conditions);
            case SKILL_BASED:
                return generateSkillBasedRule(tagRule, conditions);
            case PERFORMANCE_BASED:
                return generatePerformanceBasedRule(tagRule, conditions);
            case EDUCATION_BASED:
                return generateEducationBasedRule(tagRule, conditions);
            case LEADERSHIP_BASED:
                return generateLeadershipBasedRule(tagRule, conditions);
            case COMPOSITE:
                return generateCompositeRule(tagRule, conditions);
            case CUSTOM:
                return tagRule.getRuleContent(); // 直接返回自定义内容
            default:
                throw new IllegalArgumentException("不支持的规则模板: " + template);
        }
    }
    
    /**
     * 生成经验年限规则
     */
    private String generateExperienceBasedRule(TagRule tagRule, Map<String, Object> conditions) {
        int minYears = (Integer) conditions.getOrDefault("minYears", 0);
        int maxYears = (Integer) conditions.getOrDefault("maxYears", 50);
        String operator = (String) conditions.getOrDefault("operator", ">=");
        
        StringBuilder rule = new StringBuilder();
        rule.append("package com.rizhaosteel.droolstest.tagging.rules\n\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.model.EnhancedEmployee\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.model.EmployeeTag\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.model.Tag\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.service.TagLibraryService\n\n");
        rule.append("global TagLibraryService tagLibraryService\n\n");
        
        rule.append("rule \"").append(tagRule.getRuleName()).append("\"\n");
        rule.append("    salience ").append(tagRule.getPriority()).append("\n");
        rule.append("    when\n");
        rule.append("        $employee : EnhancedEmployee()\n");
        
        if (maxYears < 50) {
            rule.append("        eval($employee.getExperienceInfo().getTotalWorkYears() ")
                .append(operator).append(" ").append(minYears)
                .append(" && $employee.getExperienceInfo().getTotalWorkYears() <= ")
                .append(maxYears).append(")\n");
        } else {
            rule.append("        eval($employee.getExperienceInfo().getTotalWorkYears() ")
                .append(operator).append(" ").append(minYears).append(")\n");
        }
        
        rule.append("    then\n");
        rule.append("        Tag tag = tagLibraryService.getTagByCode(\"").append(tagRule.getTagCode()).append("\");\n");
        rule.append("        if (tag != null) {\n");
        rule.append("            EmployeeTag employeeTag = new EmployeeTag($employee.getId(), tag, 0.8, \"RULE_ENGINE\", \n");
        rule.append("                \"工作经验\" + $employee.getExperienceInfo().getTotalWorkYears() + \"年\");\n");
        rule.append("            $employee.addTag(employeeTag);\n");
        rule.append("            System.out.println(\"员工 \" + $employee.getName() + \" 获得标签：\" + tag.getName());\n");
        rule.append("        }\n");
        rule.append("end\n");
        
        return rule.toString();
    }
    
    /**
     * 生成技能规则
     */
    private String generateSkillBasedRule(TagRule tagRule, Map<String, Object> conditions) {
        String[] requiredSkills = (String[]) conditions.getOrDefault("requiredSkills", new String[]{});
        int skillCount = (Integer) conditions.getOrDefault("skillCount", 1);
        String matchType = (String) conditions.getOrDefault("matchType", "ANY");
        
        StringBuilder rule = new StringBuilder();
        rule.append("package com.rizhaosteel.droolstest.tagging.rules\n\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.model.EnhancedEmployee\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.model.EmployeeTag\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.model.Tag\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.service.TagLibraryService\n\n");
        rule.append("global TagLibraryService tagLibraryService\n\n");
        
        rule.append("rule \"").append(tagRule.getRuleName()).append("\"\n");
        rule.append("    salience ").append(tagRule.getPriority()).append("\n");
        rule.append("    when\n");
        rule.append("        $employee : EnhancedEmployee()\n");
        rule.append("        $ability : EnhancedEmployee.AbilityInfo() from $employee.getAbilityInfo()\n");
        
        if ("ALL".equals(matchType)) {
            // 必须包含所有技能
            for (String skill : requiredSkills) {
                rule.append("        eval($ability.getTechnicalSkills().contains(\"").append(skill).append("\"))\n");
            }
        } else {
            // 包含任意技能
            rule.append("        eval(");
            for (int i = 0; i < requiredSkills.length; i++) {
                if (i > 0) rule.append(" || ");
                rule.append("$ability.getTechnicalSkills().contains(\"").append(requiredSkills[i]).append("\")");
            }
            rule.append(")\n");
        }
        
        rule.append("    then\n");
        rule.append("        Tag tag = tagLibraryService.getTagByCode(\"").append(tagRule.getTagCode()).append("\");\n");
        rule.append("        if (tag != null) {\n");
        rule.append("            EmployeeTag employeeTag = new EmployeeTag($employee.getId(), tag, 0.8, \"RULE_ENGINE\", \n");
        rule.append("                \"具备相关技能\");\n");
        rule.append("            $employee.addTag(employeeTag);\n");
        rule.append("            System.out.println(\"员工 \" + $employee.getName() + \" 获得标签：\" + tag.getName());\n");
        rule.append("        }\n");
        rule.append("end\n");
        
        return rule.toString();
    }
    
    /**
     * 生成绩效规则
     */
    private String generatePerformanceBasedRule(TagRule tagRule, Map<String, Object> conditions) {
        int minScore = (Integer) conditions.getOrDefault("minScore", 0);
        int maxScore = (Integer) conditions.getOrDefault("maxScore", 100);
        String operator = (String) conditions.getOrDefault("operator", ">=");
        
        StringBuilder rule = new StringBuilder();
        rule.append("package com.rizhaosteel.droolstest.tagging.rules\n\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.model.EnhancedEmployee\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.model.EmployeeTag\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.model.Tag\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.service.TagLibraryService\n\n");
        rule.append("global TagLibraryService tagLibraryService\n\n");
        
        rule.append("rule \"").append(tagRule.getRuleName()).append("\"\n");
        rule.append("    salience ").append(tagRule.getPriority()).append("\n");
        rule.append("    when\n");
        rule.append("        $employee : EnhancedEmployee()\n");
        rule.append("        $evaluation : EnhancedEmployee.EvaluationInfo() from $employee.getEvaluationInfo()\n");
        
        if (maxScore < 100) {
            rule.append("        eval($evaluation.getCurrentPerformanceScore() ")
                .append(operator).append(" ").append(minScore)
                .append(" && $evaluation.getCurrentPerformanceScore() <= ")
                .append(maxScore).append(")\n");
        } else {
            rule.append("        eval($evaluation.getCurrentPerformanceScore() ")
                .append(operator).append(" ").append(minScore).append(")\n");
        }
        
        rule.append("    then\n");
        rule.append("        Tag tag = tagLibraryService.getTagByCode(\"").append(tagRule.getTagCode()).append("\");\n");
        rule.append("        if (tag != null) {\n");
        rule.append("            EmployeeTag employeeTag = new EmployeeTag($employee.getId(), tag, 0.9, \"RULE_ENGINE\", \n");
        rule.append("                \"绩效评分\" + $evaluation.getCurrentPerformanceScore());\n");
        rule.append("            $employee.addTag(employeeTag);\n");
        rule.append("            System.out.println(\"员工 \" + $employee.getName() + \" 获得标签：\" + tag.getName());\n");
        rule.append("        }\n");
        rule.append("end\n");
        
        return rule.toString();
    }
    
    /**
     * 生成学历规则
     */
    private String generateEducationBasedRule(TagRule tagRule, Map<String, Object> conditions) {
        String[] educationLevels = (String[]) conditions.getOrDefault("educationLevels", new String[]{"本科"});
        String matchType = (String) conditions.getOrDefault("matchType", "ANY");
        
        StringBuilder rule = new StringBuilder();
        rule.append("package com.rizhaosteel.droolstest.tagging.rules\n\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.model.EnhancedEmployee\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.model.EmployeeTag\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.model.Tag\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.service.TagLibraryService\n\n");
        rule.append("global TagLibraryService tagLibraryService\n\n");
        
        rule.append("rule \"").append(tagRule.getRuleName()).append("\"\n");
        rule.append("    salience ").append(tagRule.getPriority()).append("\n");
        rule.append("    when\n");
        rule.append("        $employee : EnhancedEmployee()\n");
        rule.append("        eval(");
        
        for (int i = 0; i < educationLevels.length; i++) {
            if (i > 0) rule.append(" || ");
            rule.append("\"").append(educationLevels[i]).append("\".equals($employee.getExperienceInfo().getEducation())");
        }
        rule.append(")\n");
        
        rule.append("    then\n");
        rule.append("        Tag tag = tagLibraryService.getTagByCode(\"").append(tagRule.getTagCode()).append("\");\n");
        rule.append("        if (tag != null) {\n");
        rule.append("            EmployeeTag employeeTag = new EmployeeTag($employee.getId(), tag, 0.8, \"RULE_ENGINE\", \n");
        rule.append("                \"学历：\" + $employee.getExperienceInfo().getEducation());\n");
        rule.append("            $employee.addTag(employeeTag);\n");
        rule.append("            System.out.println(\"员工 \" + $employee.getName() + \" 获得标签：\" + tag.getName());\n");
        rule.append("        }\n");
        rule.append("end\n");
        
        return rule.toString();
    }
    
    /**
     * 生成领导力规则
     */
    private String generateLeadershipBasedRule(TagRule tagRule, Map<String, Object> conditions) {
        int minLeadershipScore = (Integer) conditions.getOrDefault("minLeadershipScore", 70);
        int minManagementYears = (Integer) conditions.getOrDefault("minManagementYears", 1);
        int minTeamSize = (Integer) conditions.getOrDefault("minTeamSize", 3);
        
        StringBuilder rule = new StringBuilder();
        rule.append("package com.rizhaosteel.droolstest.tagging.rules\n\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.model.EnhancedEmployee\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.model.EmployeeTag\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.model.Tag\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.service.TagLibraryService\n\n");
        rule.append("global TagLibraryService tagLibraryService\n\n");
        
        rule.append("rule \"").append(tagRule.getRuleName()).append("\"\n");
        rule.append("    salience ").append(tagRule.getPriority()).append("\n");
        rule.append("    when\n");
        rule.append("        $employee : EnhancedEmployee()\n");
        rule.append("        $ability : EnhancedEmployee.AbilityInfo() from $employee.getAbilityInfo()\n");
        rule.append("        eval($ability.getLeadershipScore() >= ").append(minLeadershipScore).append(" && \n");
        rule.append("             $employee.getExperienceInfo().getManagementYears() >= ").append(minManagementYears).append(" &&\n");
        rule.append("             $employee.getExperienceInfo().getTeamSize() >= ").append(minTeamSize).append(")\n");
        
        rule.append("    then\n");
        rule.append("        Tag tag = tagLibraryService.getTagByCode(\"").append(tagRule.getTagCode()).append("\");\n");
        rule.append("        if (tag != null) {\n");
        rule.append("            EmployeeTag employeeTag = new EmployeeTag($employee.getId(), tag, 0.9, \"RULE_ENGINE\", \n");
        rule.append("                \"领导力评分\" + $ability.getLeadershipScore() + \"，管理经验\" + $employee.getExperienceInfo().getManagementYears() + \"年\");\n");
        rule.append("            $employee.addTag(employeeTag);\n");
        rule.append("            System.out.println(\"员工 \" + $employee.getName() + \" 获得标签：\" + tag.getName());\n");
        rule.append("        }\n");
        rule.append("end\n");
        
        return rule.toString();
    }
    
    /**
     * 生成复合条件规则
     */
    private String generateCompositeRule(TagRule tagRule, Map<String, Object> conditions) {
        // 复合条件规则需要更复杂的处理，这里提供基本框架
        StringBuilder rule = new StringBuilder();
        rule.append("package com.rizhaosteel.droolstest.tagging.rules\n\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.model.EnhancedEmployee\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.model.EmployeeTag\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.model.Tag\n");
        rule.append("import com.rizhaosteel.droolstest.tagging.service.TagLibraryService\n\n");
        rule.append("global TagLibraryService tagLibraryService\n\n");
        
        rule.append("rule \"").append(tagRule.getRuleName()).append("\"\n");
        rule.append("    salience ").append(tagRule.getPriority()).append("\n");
        rule.append("    when\n");
        rule.append("        $employee : EnhancedEmployee()\n");
        rule.append("        // 复合条件需要根据具体配置生成\n");
        rule.append("    then\n");
        rule.append("        Tag tag = tagLibraryService.getTagByCode(\"").append(tagRule.getTagCode()).append("\");\n");
        rule.append("        if (tag != null) {\n");
        rule.append("            EmployeeTag employeeTag = new EmployeeTag($employee.getId(), tag, 0.8, \"RULE_ENGINE\", \n");
        rule.append("                \"复合条件匹配\");\n");
        rule.append("            $employee.addTag(employeeTag);\n");
        rule.append("            System.out.println(\"员工 \" + $employee.getName() + \" 获得标签：\" + tag.getName());\n");
        rule.append("        }\n");
        rule.append("end\n");
        
        return rule.toString();
    }
    
    /**
     * 解析条件JSON
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> parseConditions(String conditionsJson) {
        try {
            if (conditionsJson == null || conditionsJson.trim().isEmpty()) {
                return new HashMap<String, Object>();
            }
            return objectMapper.readValue(conditionsJson, Map.class);
        } catch (Exception e) {
            System.err.println("解析条件JSON失败: " + e.getMessage());
            return new HashMap<String, Object>();
        }
    }
    
    /**
     * 验证规则语法
     */
    public boolean validateRule(String ruleContent) {
        try {
            // 这里可以添加更复杂的语法验证逻辑
            return ruleContent != null && 
                   ruleContent.contains("rule") && 
                   ruleContent.contains("when") && 
                   ruleContent.contains("then") && 
                   ruleContent.contains("end");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 获取所有可用的模板
     */
    public RuleTemplate[] getAllTemplates() {
        return RuleTemplate.values();
    }
}
