package com.rizhaosteel.droolstest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工信息实体类
 */
@TableName("employee")
public class Employee {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("employee_no")
    private String employeeNo;
    
    private String name;
    private Integer age;
    private String gender;
    private String department;
    private String position;
    
    @TableField("hire_date")
    private LocalDate hireDate;
    
    // 能力素质字段
    @TableField("technical_skills")
    private String technicalSkills; // JSON格式存储
    
    @TableField("soft_skills")
    private String softSkills; // JSON格式存储
    
    @TableField("leadership_score")
    private Integer leadershipScore;
    
    @TableField("communication_score")
    private Integer communicationScore;
    
    @TableField("innovation_score")
    private Integer innovationScore;
    
    @TableField("learning_ability")
    private Integer learningAbility;
    
    private String certifications; // JSON格式存储
    
    // 履历信息字段
    @TableField("total_work_years")
    private Integer totalWorkYears;
    
    private String education;
    
    @TableField("graduate_school")
    private String graduateSchool;
    
    private String major;
    private String industries; // JSON格式存储
    
    @TableField("previous_positions")
    private String previousPositions; // JSON格式存储
    
    @TableField("management_years")
    private Integer managementYears;
    
    @TableField("team_size")
    private Integer teamSize;
    
    // 考评信息字段
    @TableField("current_performance_score")
    private Integer currentPerformanceScore;
    
    @TableField("performance_level")
    private String performanceLevel;
    
    @TableField("performance_history")
    private String performanceHistory; // JSON格式存储
    
    @TableField("potential_rating")
    private Integer potentialRating;
    
    @TableField("competency_scores")
    private String competencyScores; // JSON格式存储
    
    @TableField("team_collaboration_score")
    private Integer teamCollaborationScore;
    
    @TableField("growth_trend")
    private String growthTrend;
    
    @TableField("last_evaluation_date")
    private LocalDate lastEvaluationDate;
    
    @TableField("strength_areas")
    private String strengthAreas; // JSON格式存储
    
    @TableField("improvement_areas")
    private String improvementAreas; // JSON格式存储
    
    // 系统字段
    @TableField("is_deleted")
    private Boolean isDeleted;
    
    @TableField("created_time")
    private LocalDateTime createdTime;
    
    @TableField("updated_time")
    private LocalDateTime updatedTime;
    
    @TableField("created_by")
    private String createdBy;
    
    @TableField("updated_by")
    private String updatedBy;
    
    // 构造函数
    public Employee() {
        this.isDeleted = false;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmployeeNo() { return employeeNo; }
    public void setEmployeeNo(String employeeNo) { this.employeeNo = employeeNo; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    
    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
    
    public String getTechnicalSkills() { return technicalSkills; }
    public void setTechnicalSkills(String technicalSkills) { this.technicalSkills = technicalSkills; }
    
    public String getSoftSkills() { return softSkills; }
    public void setSoftSkills(String softSkills) { this.softSkills = softSkills; }
    
    public Integer getLeadershipScore() { return leadershipScore; }
    public void setLeadershipScore(Integer leadershipScore) { this.leadershipScore = leadershipScore; }
    
    public Integer getCommunicationScore() { return communicationScore; }
    public void setCommunicationScore(Integer communicationScore) { this.communicationScore = communicationScore; }
    
    public Integer getInnovationScore() { return innovationScore; }
    public void setInnovationScore(Integer innovationScore) { this.innovationScore = innovationScore; }
    
    public Integer getLearningAbility() { return learningAbility; }
    public void setLearningAbility(Integer learningAbility) { this.learningAbility = learningAbility; }
    
    public String getCertifications() { return certifications; }
    public void setCertifications(String certifications) { this.certifications = certifications; }
    
    public Integer getTotalWorkYears() { return totalWorkYears; }
    public void setTotalWorkYears(Integer totalWorkYears) { this.totalWorkYears = totalWorkYears; }
    
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
    
    public String getGraduateSchool() { return graduateSchool; }
    public void setGraduateSchool(String graduateSchool) { this.graduateSchool = graduateSchool; }
    
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    
    public String getIndustries() { return industries; }
    public void setIndustries(String industries) { this.industries = industries; }
    
    public String getPreviousPositions() { return previousPositions; }
    public void setPreviousPositions(String previousPositions) { this.previousPositions = previousPositions; }
    
    public Integer getManagementYears() { return managementYears; }
    public void setManagementYears(Integer managementYears) { this.managementYears = managementYears; }
    
    public Integer getTeamSize() { return teamSize; }
    public void setTeamSize(Integer teamSize) { this.teamSize = teamSize; }
    
    public Integer getCurrentPerformanceScore() { return currentPerformanceScore; }
    public void setCurrentPerformanceScore(Integer currentPerformanceScore) { this.currentPerformanceScore = currentPerformanceScore; }
    
    public String getPerformanceLevel() { return performanceLevel; }
    public void setPerformanceLevel(String performanceLevel) { this.performanceLevel = performanceLevel; }
    
    public String getPerformanceHistory() { return performanceHistory; }
    public void setPerformanceHistory(String performanceHistory) { this.performanceHistory = performanceHistory; }
    
    public Integer getPotentialRating() { return potentialRating; }
    public void setPotentialRating(Integer potentialRating) { this.potentialRating = potentialRating; }
    
    public String getCompetencyScores() { return competencyScores; }
    public void setCompetencyScores(String competencyScores) { this.competencyScores = competencyScores; }
    
    public Integer getTeamCollaborationScore() { return teamCollaborationScore; }
    public void setTeamCollaborationScore(Integer teamCollaborationScore) { this.teamCollaborationScore = teamCollaborationScore; }
    
    public String getGrowthTrend() { return growthTrend; }
    public void setGrowthTrend(String growthTrend) { this.growthTrend = growthTrend; }
    
    public LocalDate getLastEvaluationDate() { return lastEvaluationDate; }
    public void setLastEvaluationDate(LocalDate lastEvaluationDate) { this.lastEvaluationDate = lastEvaluationDate; }
    
    public String getStrengthAreas() { return strengthAreas; }
    public void setStrengthAreas(String strengthAreas) { this.strengthAreas = strengthAreas; }
    
    public String getImprovementAreas() { return improvementAreas; }
    public void setImprovementAreas(String improvementAreas) { this.improvementAreas = improvementAreas; }
    
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
    
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    
    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
}
