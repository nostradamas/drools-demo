package com.rizhaosteel.droolstest.tagging.model;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * 增强的员工实体，包含多维度信息
 */
public class EnhancedEmployee {
    private Long id;
    private String employeeNo; // 员工编号
    private String name;
    private int age;
    private String gender;
    private String department;
    private String position;
    private LocalDate hireDate;
    
    // 能力素质信息
    private AbilityInfo abilityInfo;
    
    // 履历信息
    private ExperienceInfo experienceInfo;
    
    // 考评信息
    private EvaluationInfo evaluationInfo;
    
    // 当前标签列表
    private List<EmployeeTag> currentTags;
    
    public EnhancedEmployee() {
        this.abilityInfo = new AbilityInfo();
        this.experienceInfo = new ExperienceInfo();
        this.evaluationInfo = new EvaluationInfo();
        this.currentTags = new ArrayList<>();
    }
    
    // 能力素质信息内部类
    public static class AbilityInfo {
        private List<String> technicalSkills; // 技术技能
        private Map<String, Integer> skillLevels; // 技能等级 1-5
        private List<String> softSkills; // 软技能
        private int leadershipScore; // 领导力评分 1-100
        private int communicationScore; // 沟通能力评分 1-100
        private int innovationScore; // 创新能力评分 1-100
        private int learningAbility; // 学习能力评分 1-100
        private List<String> certifications; // 认证证书
        
        public AbilityInfo() {
            this.technicalSkills = new ArrayList<>();
            this.skillLevels = new HashMap<>();
            this.softSkills = new ArrayList<>();
        }
        
        // Getters and Setters
        public List<String> getTechnicalSkills() { return technicalSkills; }
        public void setTechnicalSkills(List<String> technicalSkills) { this.technicalSkills = technicalSkills; }
        
        public Map<String, Integer> getSkillLevels() { return skillLevels; }
        public void setSkillLevels(Map<String, Integer> skillLevels) { this.skillLevels = skillLevels; }
        
        public List<String> getSoftSkills() { return softSkills; }
        public void setSoftSkills(List<String> softSkills) { this.softSkills = softSkills; }
        
        public int getLeadershipScore() { return leadershipScore; }
        public void setLeadershipScore(int leadershipScore) { this.leadershipScore = leadershipScore; }
        
        public int getCommunicationScore() { return communicationScore; }
        public void setCommunicationScore(int communicationScore) { this.communicationScore = communicationScore; }
        
        public int getInnovationScore() { return innovationScore; }
        public void setInnovationScore(int innovationScore) { this.innovationScore = innovationScore; }
        
        public int getLearningAbility() { return learningAbility; }
        public void setLearningAbility(int learningAbility) { this.learningAbility = learningAbility; }
        
        public List<String> getCertifications() { return certifications; }
        public void setCertifications(List<String> certifications) { this.certifications = certifications; }
    }
    
    // 履历信息内部类
    public static class ExperienceInfo {
        private int totalWorkYears; // 总工作年限
        private String education; // 最高学历
        private String graduateSchool; // 毕业院校
        private String major; // 专业
        private List<String> industries; // 行业经验
        private List<ProjectExperience> projects; // 项目经历
        private List<String> previousPositions; // 历任职位
        private int managementYears; // 管理年限
        private int teamSize; // 管理团队规模
        
        public ExperienceInfo() {
            this.industries = new ArrayList<>();
            this.projects = new ArrayList<>();
            this.previousPositions = new ArrayList<>();
        }
        
        // Getters and Setters
        public int getTotalWorkYears() { return totalWorkYears; }
        public void setTotalWorkYears(int totalWorkYears) { this.totalWorkYears = totalWorkYears; }
        
        public String getEducation() { return education; }
        public void setEducation(String education) { this.education = education; }
        
        public String getGraduateSchool() { return graduateSchool; }
        public void setGraduateSchool(String graduateSchool) { this.graduateSchool = graduateSchool; }
        
        public String getMajor() { return major; }
        public void setMajor(String major) { this.major = major; }
        
        public List<String> getIndustries() { return industries; }
        public void setIndustries(List<String> industries) { this.industries = industries; }
        
        public List<ProjectExperience> getProjects() { return projects; }
        public void setProjects(List<ProjectExperience> projects) { this.projects = projects; }
        
        public List<String> getPreviousPositions() { return previousPositions; }
        public void setPreviousPositions(List<String> previousPositions) { this.previousPositions = previousPositions; }
        
        public int getManagementYears() { return managementYears; }
        public void setManagementYears(int managementYears) { this.managementYears = managementYears; }
        
        public int getTeamSize() { return teamSize; }
        public void setTeamSize(int teamSize) { this.teamSize = teamSize; }
    }
    
    // 考评信息内部类
    public static class EvaluationInfo {
        private int currentPerformanceScore; // 当前绩效分数
        private String performanceLevel; // 绩效等级：A/B/C/D
        private List<Integer> performanceHistory; // 历史绩效
        private int potentialRating; // 潜力评级 1-5
        private Map<String, Integer> competencyScores; // 胜任力评分
        private int teamCollaborationScore; // 团队协作评分
        private String growthTrend; // 成长趋势：上升/稳定/下降
        private LocalDate lastEvaluationDate; // 最近考评日期
        private List<String> strengthAreas; // 优势领域
        private List<String> improvementAreas; // 改进领域
        
        public EvaluationInfo() {
            this.performanceHistory = new ArrayList<>();
            this.competencyScores = new HashMap<>();
            this.strengthAreas = new ArrayList<>();
            this.improvementAreas = new ArrayList<>();
        }
        
        // Getters and Setters
        public int getCurrentPerformanceScore() { return currentPerformanceScore; }
        public void setCurrentPerformanceScore(int currentPerformanceScore) { this.currentPerformanceScore = currentPerformanceScore; }
        
        public String getPerformanceLevel() { return performanceLevel; }
        public void setPerformanceLevel(String performanceLevel) { this.performanceLevel = performanceLevel; }
        
        public List<Integer> getPerformanceHistory() { return performanceHistory; }
        public void setPerformanceHistory(List<Integer> performanceHistory) { this.performanceHistory = performanceHistory; }
        
        public int getPotentialRating() { return potentialRating; }
        public void setPotentialRating(int potentialRating) { this.potentialRating = potentialRating; }
        
        public Map<String, Integer> getCompetencyScores() { return competencyScores; }
        public void setCompetencyScores(Map<String, Integer> competencyScores) { this.competencyScores = competencyScores; }
        
        public int getTeamCollaborationScore() { return teamCollaborationScore; }
        public void setTeamCollaborationScore(int teamCollaborationScore) { this.teamCollaborationScore = teamCollaborationScore; }
        
        public String getGrowthTrend() { return growthTrend; }
        public void setGrowthTrend(String growthTrend) { this.growthTrend = growthTrend; }
        
        public LocalDate getLastEvaluationDate() { return lastEvaluationDate; }
        public void setLastEvaluationDate(LocalDate lastEvaluationDate) { this.lastEvaluationDate = lastEvaluationDate; }
        
        public List<String> getStrengthAreas() { return strengthAreas; }
        public void setStrengthAreas(List<String> strengthAreas) { this.strengthAreas = strengthAreas; }
        
        public List<String> getImprovementAreas() { return improvementAreas; }
        public void setImprovementAreas(List<String> improvementAreas) { this.improvementAreas = improvementAreas; }
    }
    
    // 项目经验内部类
    public static class ProjectExperience {
        private String projectName;
        private String role;
        private int durationMonths;
        private String technology;
        private String achievement;
        
        public ProjectExperience() {}
        
        public ProjectExperience(String projectName, String role, int durationMonths, String technology, String achievement) {
            this.projectName = projectName;
            this.role = role;
            this.durationMonths = durationMonths;
            this.technology = technology;
            this.achievement = achievement;
        }
        
        // Getters and Setters
        public String getProjectName() { return projectName; }
        public void setProjectName(String projectName) { this.projectName = projectName; }
        
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        
        public int getDurationMonths() { return durationMonths; }
        public void setDurationMonths(int durationMonths) { this.durationMonths = durationMonths; }
        
        public String getTechnology() { return technology; }
        public void setTechnology(String technology) { this.technology = technology; }
        
        public String getAchievement() { return achievement; }
        public void setAchievement(String achievement) { this.achievement = achievement; }
    }
    
    // 主类的 Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmployeeNo() { return employeeNo; }
    public void setEmployeeNo(String employeeNo) { this.employeeNo = employeeNo; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    
    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
    
    public AbilityInfo getAbilityInfo() { return abilityInfo; }
    public void setAbilityInfo(AbilityInfo abilityInfo) { this.abilityInfo = abilityInfo; }
    
    public ExperienceInfo getExperienceInfo() { return experienceInfo; }
    public void setExperienceInfo(ExperienceInfo experienceInfo) { this.experienceInfo = experienceInfo; }
    
    public EvaluationInfo getEvaluationInfo() { return evaluationInfo; }
    public void setEvaluationInfo(EvaluationInfo evaluationInfo) { this.evaluationInfo = evaluationInfo; }
    
    public List<EmployeeTag> getCurrentTags() { return currentTags; }
    public void setCurrentTags(List<EmployeeTag> currentTags) { this.currentTags = currentTags; }
    
    // 便利方法
    public void addTag(EmployeeTag tag) {
        this.currentTags.add(tag);
    }
    
    public boolean hasTag(String tagCode) {
        return currentTags.stream()
                .anyMatch(tag -> tag.getTagCode().equals(tagCode) && tag.isActive() && !tag.isExpired());
    }
    
    public List<EmployeeTag> getTagsByDimension(String dimension) {
        return currentTags.stream()
                .filter(tag -> dimension.equals(tag.getDimension()) && tag.isActive() && !tag.isExpired())
                .toList();
    }
}
