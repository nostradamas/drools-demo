package com.rizhaosteel.droolstest.tagging.controller;

import com.rizhaosteel.droolstest.tagging.model.EnhancedEmployee;
import com.rizhaosteel.droolstest.tagging.model.EmployeeTag;
import com.rizhaosteel.droolstest.tagging.service.IntegratedTaggingService;
import com.rizhaosteel.droolstest.tagging.service.DynamicRuleService;
import com.rizhaosteel.droolstest.tagging.service.TagLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

/**
 * 集成标签控制器 - 展示完整的动态标签系统
 */
@RestController
@RequestMapping("/api/integrated-tagging")
@CrossOrigin(origins = "*")
public class IntegratedTaggingController {
    
    @Autowired
    private IntegratedTaggingService integratedTaggingService;
    
    @Autowired
    private DynamicRuleService dynamicRuleService;
    
    @Autowired
    private TagLibraryService tagLibraryService;
    
    /**
     * 使用动态规则给员工打标签
     */
    @PostMapping("/tag-employee-dynamic")
    public EnhancedEmployee tagEmployeeWithDynamicRules(@RequestBody EnhancedEmployee employee) {
        return integratedTaggingService.tagEmployeeWithDynamicRules(employee);
    }
    
    /**
     * 批量使用动态规则打标签
     */
    @PostMapping("/batch-tag-dynamic")
    public List<EnhancedEmployee> batchTagWithDynamicRules(@RequestBody List<EnhancedEmployee> employees) {
        return integratedTaggingService.batchTagEmployeesWithDynamicRules(employees);
    }
    
    /**
     * 完整演示 - 展示标签库管理和动态规则的完整流程
     */
    @GetMapping("/full-demo")
    public FullDemoResponse runFullDemo() {
        FullDemoResponse response = new FullDemoResponse();
        
        // 1. 初始化默认规则
        dynamicRuleService.initializeDefaultRules();
        
        // 2. 获取标签库信息
        response.setTagLibraryStats(tagLibraryService.getTagStatistics());
        response.setRuleStats(dynamicRuleService.getRuleStatistics());
        
        // 3. 创建测试员工
        List<EnhancedEmployee> employees = createTestEmployees();
        
        // 4. 使用动态规则打标签
        List<EnhancedEmployee> taggedEmployees = integratedTaggingService.batchTagEmployeesWithDynamicRules(employees);
        response.setTaggedEmployees(taggedEmployees);
        
        // 5. 生成标签分析报告
        Map<String, Object> tagAnalysis = generateTagAnalysis(taggedEmployees);
        response.setTagAnalysis(tagAnalysis);
        
        // 6. 生成规则执行报告
        Map<String, Object> ruleExecutionReport = generateRuleExecutionReport();
        response.setRuleExecutionReport(ruleExecutionReport);
        
        return response;
    }
    
    /**
     * 系统状态检查
     */
    @GetMapping("/system-status")
    public SystemStatusResponse getSystemStatus() {
        SystemStatusResponse status = new SystemStatusResponse();
        
        // 标签库状态
        Map<String, Object> tagLibraryStatus = new HashMap<>();
        tagLibraryStatus.put("totalTags", tagLibraryService.getAllTags().size());
        tagLibraryStatus.put("activeTags", tagLibraryService.getAllTags().stream().filter(tag -> tag.isActive()).count());
        tagLibraryStatus.put("statistics", tagLibraryService.getTagStatistics());
        status.setTagLibraryStatus(tagLibraryStatus);
        
        // 规则引擎状态
        Map<String, Object> ruleEngineStatus = new HashMap<>();
        ruleEngineStatus.put("totalRules", dynamicRuleService.getAllRules().size());
        ruleEngineStatus.put("activeRules", dynamicRuleService.getAllRules().stream().filter(rule -> rule.isActive()).count());
        ruleEngineStatus.put("statistics", dynamicRuleService.getRuleStatistics());
        status.setRuleEngineStatus(ruleEngineStatus);
        
        // 系统健康状态
        status.setSystemHealth("HEALTHY");
        status.setLastUpdateTime(new Date());
        
        return status;
    }
    
    /**
     * 创建测试员工数据
     */
    private List<EnhancedEmployee> createTestEmployees() {
        List<EnhancedEmployee> employees = new ArrayList<>();
        
        // 员工1：技术专家
        EnhancedEmployee emp1 = new EnhancedEmployee();
        emp1.setId(1L);
        emp1.setName("张技术专家");
        emp1.setAge(32);
        emp1.setDepartment("技术部");
        emp1.setPosition("高级架构师");
        
        // 能力信息
        emp1.getAbilityInfo().setTechnicalSkills(Arrays.asList("Java", "Python", "微服务", "分布式", "数据库", "Redis"));
        emp1.getAbilityInfo().getSkillLevels().put("Java", 5);
        emp1.getAbilityInfo().getSkillLevels().put("Python", 4);
        emp1.getAbilityInfo().setLeadershipScore(88);
        emp1.getAbilityInfo().setCommunicationScore(85);
        emp1.getAbilityInfo().setInnovationScore(90);
        
        // 履历信息
        emp1.getExperienceInfo().setTotalWorkYears(9);
        emp1.getExperienceInfo().setEducation("硕士");
        emp1.getExperienceInfo().setGraduateSchool("清华大学");
        emp1.getExperienceInfo().setManagementYears(3);
        emp1.getExperienceInfo().setTeamSize(8);
        
        // 考评信息
        emp1.getEvaluationInfo().setCurrentPerformanceScore(92);
        emp1.getEvaluationInfo().setPerformanceLevel("A");
        emp1.getEvaluationInfo().setPotentialRating(5);
        emp1.getEvaluationInfo().setTeamCollaborationScore(90);
        emp1.getEvaluationInfo().setGrowthTrend("上升");
        
        employees.add(emp1);
        
        // 员工2：前端专家
        EnhancedEmployee emp2 = new EnhancedEmployee();
        emp2.setId(2L);
        emp2.setName("李前端专家");
        emp2.setAge(28);
        emp2.setDepartment("技术部");
        emp2.setPosition("前端架构师");
        
        emp2.getAbilityInfo().setTechnicalSkills(Arrays.asList("JavaScript", "React", "Vue", "CSS", "TypeScript"));
        emp2.getAbilityInfo().getSkillLevels().put("JavaScript", 5);
        emp2.getAbilityInfo().getSkillLevels().put("React", 5);
        emp2.getAbilityInfo().setLeadershipScore(75);
        emp2.getAbilityInfo().setCommunicationScore(88);
        
        emp2.getExperienceInfo().setTotalWorkYears(6);
        emp2.getExperienceInfo().setEducation("本科");
        emp2.getExperienceInfo().setGraduateSchool("北京大学");
        
        emp2.getEvaluationInfo().setCurrentPerformanceScore(88);
        emp2.getEvaluationInfo().setPerformanceLevel("A");
        emp2.getEvaluationInfo().setPotentialRating(4);
        
        employees.add(emp2);
        
        // 员工3：初级开发者
        EnhancedEmployee emp3 = new EnhancedEmployee();
        emp3.setId(3L);
        emp3.setName("王初级开发");
        emp3.setAge(24);
        emp3.setDepartment("技术部");
        emp3.setPosition("Java开发工程师");
        
        emp3.getAbilityInfo().setTechnicalSkills(Arrays.asList("Java", "Spring", "MySQL"));
        emp3.getAbilityInfo().getSkillLevels().put("Java", 3);
        emp3.getAbilityInfo().setLeadershipScore(60);
        emp3.getAbilityInfo().setCommunicationScore(70);
        
        emp3.getExperienceInfo().setTotalWorkYears(1);
        emp3.getExperienceInfo().setEducation("本科");
        
        emp3.getEvaluationInfo().setCurrentPerformanceScore(78);
        emp3.getEvaluationInfo().setPerformanceLevel("B");
        emp3.getEvaluationInfo().setPotentialRating(4);
        
        employees.add(emp3);
        
        return employees;
    }
    
    /**
     * 生成标签分析报告
     */
    private Map<String, Object> generateTagAnalysis(List<EnhancedEmployee> employees) {
        Map<String, Object> analysis = new HashMap<>();
        
        // 统计每个员工的标签数量
        Map<String, Integer> employeeTagCounts = new HashMap<>();
        Map<String, Map<String, Integer>> employeeDimensionCounts = new HashMap<>();
        
        for (EnhancedEmployee employee : employees) {
            List<EmployeeTag> activeTags = employee.getCurrentTags().stream()
                    .filter(tag -> tag.isActive() && !tag.isExpired())
                    .toList();
            
            employeeTagCounts.put(employee.getName(), activeTags.size());
            
            // 按维度统计
            Map<String, Integer> dimensionCounts = new HashMap<>();
            for (EmployeeTag tag : activeTags) {
                dimensionCounts.merge(tag.getDimension(), 1, Integer::sum);
            }
            employeeDimensionCounts.put(employee.getName(), dimensionCounts);
        }
        
        analysis.put("employeeTagCounts", employeeTagCounts);
        analysis.put("employeeDimensionCounts", employeeDimensionCounts);
        
        // 统计最常见的标签
        Map<String, Integer> tagFrequency = new HashMap<>();
        for (EnhancedEmployee employee : employees) {
            for (EmployeeTag tag : employee.getCurrentTags()) {
                if (tag.isActive() && !tag.isExpired()) {
                    tagFrequency.merge(tag.getTagCode(), 1, Integer::sum);
                }
            }
        }
        analysis.put("tagFrequency", tagFrequency);
        
        return analysis;
    }
    
    /**
     * 生成规则执行报告
     */
    private Map<String, Object> generateRuleExecutionReport() {
        Map<String, Object> report = new HashMap<>();
        
        List<com.rizhaosteel.droolstest.tagging.model.TagRule> rules = dynamicRuleService.getAllRules();
        
        // 统计规则执行次数
        Map<String, Long> ruleExecutionCounts = new HashMap<>();
        long totalExecutions = 0;
        
        for (com.rizhaosteel.droolstest.tagging.model.TagRule rule : rules) {
            ruleExecutionCounts.put(rule.getRuleCode(), rule.getExecutionCount());
            totalExecutions += rule.getExecutionCount();
        }
        
        report.put("ruleExecutionCounts", ruleExecutionCounts);
        report.put("totalExecutions", totalExecutions);
        report.put("activeRulesCount", rules.stream().filter(com.rizhaosteel.droolstest.tagging.model.TagRule::isActive).count());
        report.put("totalRulesCount", rules.size());
        
        return report;
    }
    
    // 内部类定义
    public static class FullDemoResponse {
        private Map<String, Object> tagLibraryStats;
        private Map<String, Object> ruleStats;
        private List<EnhancedEmployee> taggedEmployees;
        private Map<String, Object> tagAnalysis;
        private Map<String, Object> ruleExecutionReport;
        
        // Getters and Setters
        public Map<String, Object> getTagLibraryStats() { return tagLibraryStats; }
        public void setTagLibraryStats(Map<String, Object> tagLibraryStats) { this.tagLibraryStats = tagLibraryStats; }
        public Map<String, Object> getRuleStats() { return ruleStats; }
        public void setRuleStats(Map<String, Object> ruleStats) { this.ruleStats = ruleStats; }
        public List<EnhancedEmployee> getTaggedEmployees() { return taggedEmployees; }
        public void setTaggedEmployees(List<EnhancedEmployee> taggedEmployees) { this.taggedEmployees = taggedEmployees; }
        public Map<String, Object> getTagAnalysis() { return tagAnalysis; }
        public void setTagAnalysis(Map<String, Object> tagAnalysis) { this.tagAnalysis = tagAnalysis; }
        public Map<String, Object> getRuleExecutionReport() { return ruleExecutionReport; }
        public void setRuleExecutionReport(Map<String, Object> ruleExecutionReport) { this.ruleExecutionReport = ruleExecutionReport; }
    }
    
    public static class SystemStatusResponse {
        private Map<String, Object> tagLibraryStatus;
        private Map<String, Object> ruleEngineStatus;
        private String systemHealth;
        private Date lastUpdateTime;
        
        // Getters and Setters
        public Map<String, Object> getTagLibraryStatus() { return tagLibraryStatus; }
        public void setTagLibraryStatus(Map<String, Object> tagLibraryStatus) { this.tagLibraryStatus = tagLibraryStatus; }
        public Map<String, Object> getRuleEngineStatus() { return ruleEngineStatus; }
        public void setRuleEngineStatus(Map<String, Object> ruleEngineStatus) { this.ruleEngineStatus = ruleEngineStatus; }
        public String getSystemHealth() { return systemHealth; }
        public void setSystemHealth(String systemHealth) { this.systemHealth = systemHealth; }
        public Date getLastUpdateTime() { return lastUpdateTime; }
        public void setLastUpdateTime(Date lastUpdateTime) { this.lastUpdateTime = lastUpdateTime; }
    }
}
