package com.rizhaosteel.droolstest.tagging.controller;

import com.rizhaosteel.droolstest.tagging.model.*;
import com.rizhaosteel.droolstest.tagging.service.EnhancedEmployeeTaggingService;
import com.rizhaosteel.droolstest.tagging.service.TagLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

/**
 * 增强的员工标签控制器
 */
@RestController
@RequestMapping("/api/enhanced-tagging")
@CrossOrigin(origins = "*")
public class EnhancedEmployeeTaggingController {
    
    @Autowired
    private EnhancedEmployeeTaggingService taggingService;
    
    @Autowired
    private TagLibraryService tagLibraryService;
    
    /**
     * 给员工打标签
     */
    @PostMapping("/tag-employee")
    public EnhancedEmployee tagEmployee(@RequestBody EnhancedEmployee employee) {
        return taggingService.tagEmployee(employee);
    }
    
    /**
     * 批量给员工打标签
     */
    @PostMapping("/batch-tag")
    public List<EnhancedEmployee> batchTagEmployees(@RequestBody List<EnhancedEmployee> employees) {
        return taggingService.batchTagEmployees(employees);
    }
    
    /**
     * 获取员工标签统计
     */
    @GetMapping("/employee/{employeeId}/tag-stats")
    public Map<String, Object> getEmployeeTagStatistics(@PathVariable Long employeeId) {
        // 这里应该从数据库获取员工信息
        EnhancedEmployee employee = createSampleEmployee(employeeId);
        return taggingService.getEmployeeTagStatistics(employee);
    }
    
    /**
     * 按维度获取员工标签
     */
    @GetMapping("/employee/{employeeId}/tags-by-dimension")
    public Map<String, List<EmployeeTag>> getEmployeeTagsByDimension(@PathVariable Long employeeId) {
        EnhancedEmployee employee = createSampleEmployee(employeeId);
        return taggingService.getEmployeeTagsByDimension(employee);
    }
    
    /**
     * 手动添加标签
     */
    @PostMapping("/employee/{employeeId}/add-tag")
    public void addManualTag(@PathVariable Long employeeId, @RequestBody AddTagRequest request) {
        taggingService.addManualTag(employeeId, request.getTagCode(), request.getReason());
    }
    
    /**
     * 获取标签库信息
     */
    @GetMapping("/tag-library")
    public TagLibraryResponse getTagLibrary() {
        TagLibraryResponse response = new TagLibraryResponse();
        response.setAllTags(tagLibraryService.getAllTags());
        response.setAbilityTags(tagLibraryService.getTagsByDimension("ABILITY"));
        response.setExperienceTags(tagLibraryService.getTagsByDimension("EXPERIENCE"));
        response.setEvaluationTags(tagLibraryService.getTagsByDimension("EVALUATION"));
        response.setStatistics(tagLibraryService.getTagStatistics());
        return response;
    }
    
    /**
     * 搜索标签
     */
    @GetMapping("/tag-library/search")
    public List<Tag> searchTags(@RequestParam String keyword) {
        return tagLibraryService.searchTags(keyword);
    }
    
    /**
     * 获取培训推荐
     */
    @GetMapping("/employee/{employeeId}/training-recommendations")
    public List<String> getTrainingRecommendations(@PathVariable Long employeeId) {
        EnhancedEmployee employee = createSampleEmployee(employeeId);
        employee = taggingService.tagEmployee(employee);
        return taggingService.recommendTrainingByTags(employee);
    }
    
    /**
     * 完整演示
     */
    @GetMapping("/demo")
    public DemoResponse runDemo() {
        DemoResponse response = new DemoResponse();
        
        // 创建测试员工
        List<EnhancedEmployee> employees = createSampleEmployees();
        
        // 给所有员工打标签
        List<EnhancedEmployee> taggedEmployees = taggingService.batchTagEmployees(employees);
        response.setTaggedEmployees(taggedEmployees);
        
        // 生成标签统计报告
        Map<String, Object> overallStats = new HashMap<>();
        for (EnhancedEmployee employee : taggedEmployees) {
            Map<String, Object> employeeStats = taggingService.getEmployeeTagStatistics(employee);
            overallStats.put(employee.getName(), employeeStats);
        }
        response.setTagStatistics(overallStats);
        
        // 生成培训推荐
        Map<String, List<String>> trainingRecommendations = new HashMap<>();
        for (EnhancedEmployee employee : taggedEmployees) {
            List<String> recommendations = taggingService.recommendTrainingByTags(employee);
            trainingRecommendations.put(employee.getName(), recommendations);
        }
        response.setTrainingRecommendations(trainingRecommendations);
        
        return response;
    }
    
    /**
     * 创建示例员工数据
     */
    private List<EnhancedEmployee> createSampleEmployees() {
        List<EnhancedEmployee> employees = new ArrayList<>();
        
        // 员工1：技术专家
        EnhancedEmployee emp1 = new EnhancedEmployee();
        emp1.setId(1L);
        emp1.setEmployeeNo("EMP001");
        emp1.setName("张技术");
        emp1.setAge(32);
        emp1.setGender("男");
        emp1.setDepartment("技术部");
        emp1.setPosition("高级架构师");
        emp1.setHireDate(LocalDate.of(2015, 3, 15));
        
        // 能力信息
        EnhancedEmployee.AbilityInfo ability1 = emp1.getAbilityInfo();
        ability1.setTechnicalSkills(Arrays.asList("Java", "Python", "微服务", "分布式", "数据库", "Redis"));
        ability1.getSkillLevels().put("Java", 5);
        ability1.getSkillLevels().put("Python", 4);
        ability1.getSkillLevels().put("微服务", 5);
        ability1.setSoftSkills(Arrays.asList("问题解决", "团队协作", "技术分享"));
        ability1.setLeadershipScore(88);
        ability1.setCommunicationScore(85);
        ability1.setInnovationScore(90);
        ability1.setLearningAbility(92);
        ability1.setCertifications(Arrays.asList("AWS认证", "Oracle认证"));
        
        // 履历信息
        EnhancedEmployee.ExperienceInfo experience1 = emp1.getExperienceInfo();
        experience1.setTotalWorkYears(9);
        experience1.setEducation("硕士");
        experience1.setGraduateSchool("清华大学");
        experience1.setMajor("计算机科学");
        experience1.setIndustries(Arrays.asList("金融", "电商", "云计算"));
        experience1.setPreviousPositions(Arrays.asList("Java开发工程师", "高级开发工程师", "技术专家"));
        experience1.setManagementYears(3);
        experience1.setTeamSize(8);
        
        // 添加项目经验
        experience1.getProjects().add(new EnhancedEmployee.ProjectExperience(
            "分布式支付系统", "技术负责人", 18, "Java+微服务", "系统性能提升300%"));
        experience1.getProjects().add(new EnhancedEmployee.ProjectExperience(
            "大数据平台", "架构师", 12, "Python+Spark", "处理能力提升10倍"));
        
        // 考评信息
        EnhancedEmployee.EvaluationInfo evaluation1 = emp1.getEvaluationInfo();
        evaluation1.setCurrentPerformanceScore(92);
        evaluation1.setPerformanceLevel("A");
        evaluation1.setPerformanceHistory(Arrays.asList(85, 88, 90, 92));
        evaluation1.setPotentialRating(5);
        evaluation1.getCompetencyScores().put("技术能力", 95);
        evaluation1.getCompetencyScores().put("领导能力", 88);
        evaluation1.getCompetencyScores().put("沟通能力", 85);
        evaluation1.setTeamCollaborationScore(90);
        evaluation1.setGrowthTrend("上升");
        evaluation1.setLastEvaluationDate(LocalDate.of(2024, 12, 1));
        evaluation1.setStrengthAreas(Arrays.asList("技术架构", "团队管理", "创新思维"));
        evaluation1.setImprovementAreas(Arrays.asList("商业理解"));
        
        employees.add(emp1);
        
        // 员工2：前端专家
        EnhancedEmployee emp2 = new EnhancedEmployee();
        emp2.setId(2L);
        emp2.setEmployeeNo("EMP002");
        emp2.setName("李前端");
        emp2.setAge(28);
        emp2.setGender("女");
        emp2.setDepartment("技术部");
        emp2.setPosition("前端架构师");
        emp2.setHireDate(LocalDate.of(2018, 6, 1));
        
        // 能力信息
        EnhancedEmployee.AbilityInfo ability2 = emp2.getAbilityInfo();
        ability2.setTechnicalSkills(Arrays.asList("JavaScript", "React", "Vue", "CSS", "TypeScript", "Webpack"));
        ability2.getSkillLevels().put("JavaScript", 5);
        ability2.getSkillLevels().put("React", 5);
        ability2.getSkillLevels().put("Vue", 4);
        ability2.getSkillLevels().put("CSS", 5);
        ability2.setSoftSkills(Arrays.asList("UI设计", "用户体验", "创意思维"));
        ability2.setLeadershipScore(75);
        ability2.setCommunicationScore(88);
        ability2.setInnovationScore(85);
        ability2.setLearningAbility(90);
        
        // 履历信息
        EnhancedEmployee.ExperienceInfo experience2 = emp2.getExperienceInfo();
        experience2.setTotalWorkYears(6);
        experience2.setEducation("本科");
        experience2.setGraduateSchool("北京大学");
        experience2.setMajor("软件工程");
        experience2.setIndustries(Arrays.asList("互联网", "电商"));
        experience2.setPreviousPositions(Arrays.asList("前端开发工程师", "高级前端工程师"));
        experience2.setManagementYears(1);
        experience2.setTeamSize(4);
        
        // 考评信息
        EnhancedEmployee.EvaluationInfo evaluation2 = emp2.getEvaluationInfo();
        evaluation2.setCurrentPerformanceScore(88);
        evaluation2.setPerformanceLevel("A");
        evaluation2.setPerformanceHistory(Arrays.asList(80, 83, 85, 88));
        evaluation2.setPotentialRating(4);
        evaluation2.setTeamCollaborationScore(92);
        evaluation2.setGrowthTrend("上升");
        evaluation2.setLastEvaluationDate(LocalDate.of(2024, 12, 1));
        
        employees.add(emp2);
        
        // 员工3：初级开发者
        EnhancedEmployee emp3 = new EnhancedEmployee();
        emp3.setId(3L);
        emp3.setEmployeeNo("EMP003");
        emp3.setName("王新人");
        emp3.setAge(24);
        emp3.setGender("男");
        emp3.setDepartment("技术部");
        emp3.setPosition("Java开发工程师");
        emp3.setHireDate(LocalDate.of(2023, 7, 1));
        
        // 能力信息
        EnhancedEmployee.AbilityInfo ability3 = emp3.getAbilityInfo();
        ability3.setTechnicalSkills(Arrays.asList("Java", "Spring", "MySQL"));
        ability3.getSkillLevels().put("Java", 3);
        ability3.getSkillLevels().put("Spring", 2);
        ability3.getSkillLevels().put("MySQL", 3);
        ability3.setSoftSkills(Arrays.asList("学习能力", "责任心"));
        ability3.setLeadershipScore(60);
        ability3.setCommunicationScore(70);
        ability3.setInnovationScore(75);
        ability3.setLearningAbility(88);
        
        // 履历信息
        EnhancedEmployee.ExperienceInfo experience3 = emp3.getExperienceInfo();
        experience3.setTotalWorkYears(1);
        experience3.setEducation("本科");
        experience3.setGraduateSchool("华中科技大学");
        experience3.setMajor("计算机科学");
        experience3.setIndustries(Arrays.asList("互联网"));
        experience3.setPreviousPositions(Arrays.asList("实习生"));
        experience3.setManagementYears(0);
        experience3.setTeamSize(0);
        
        // 考评信息
        EnhancedEmployee.EvaluationInfo evaluation3 = emp3.getEvaluationInfo();
        evaluation3.setCurrentPerformanceScore(78);
        evaluation3.setPerformanceLevel("B");
        evaluation3.setPerformanceHistory(Arrays.asList(75, 78));
        evaluation3.setPotentialRating(4);
        evaluation3.setTeamCollaborationScore(85);
        evaluation3.setGrowthTrend("上升");
        evaluation3.setLastEvaluationDate(LocalDate.of(2024, 12, 1));
        
        employees.add(emp3);
        
        return employees;
    }
    
    private EnhancedEmployee createSampleEmployee(Long employeeId) {
        List<EnhancedEmployee> employees = createSampleEmployees();
        return employees.stream()
                .filter(emp -> emp.getId().equals(employeeId))
                .findFirst()
                .orElse(employees.get(0));
    }
    
    // 内部类定义
    public static class AddTagRequest {
        private String tagCode;
        private String reason;
        
        public String getTagCode() { return tagCode; }
        public void setTagCode(String tagCode) { this.tagCode = tagCode; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
    
    public static class TagLibraryResponse {
        private List<Tag> allTags;
        private List<Tag> abilityTags;
        private List<Tag> experienceTags;
        private List<Tag> evaluationTags;
        private Map<String, Object> statistics;
        
        public List<Tag> getAllTags() { return allTags; }
        public void setAllTags(List<Tag> allTags) { this.allTags = allTags; }
        public List<Tag> getAbilityTags() { return abilityTags; }
        public void setAbilityTags(List<Tag> abilityTags) { this.abilityTags = abilityTags; }
        public List<Tag> getExperienceTags() { return experienceTags; }
        public void setExperienceTags(List<Tag> experienceTags) { this.experienceTags = experienceTags; }
        public List<Tag> getEvaluationTags() { return evaluationTags; }
        public void setEvaluationTags(List<Tag> evaluationTags) { this.evaluationTags = evaluationTags; }
        public Map<String, Object> getStatistics() { return statistics; }
        public void setStatistics(Map<String, Object> statistics) { this.statistics = statistics; }
    }
    
    public static class DemoResponse {
        private List<EnhancedEmployee> taggedEmployees;
        private Map<String, Object> tagStatistics;
        private Map<String, List<String>> trainingRecommendations;
        
        public List<EnhancedEmployee> getTaggedEmployees() { return taggedEmployees; }
        public void setTaggedEmployees(List<EnhancedEmployee> taggedEmployees) { this.taggedEmployees = taggedEmployees; }
        public Map<String, Object> getTagStatistics() { return tagStatistics; }
        public void setTagStatistics(Map<String, Object> tagStatistics) { this.tagStatistics = tagStatistics; }
        public Map<String, List<String>> getTrainingRecommendations() { return trainingRecommendations; }
        public void setTrainingRecommendations(Map<String, List<String>> trainingRecommendations) { this.trainingRecommendations = trainingRecommendations; }
    }
}
