package com.rizhaosteel.droolstest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 项目经验实体类
 */
@TableName("project_experience")
public class ProjectExperience {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("employee_id")
    private Long employeeId;
    
    @TableField("project_name")
    private String projectName;
    
    private String role;
    
    @TableField("start_date")
    private LocalDate startDate;
    
    @TableField("end_date")
    private LocalDate endDate;
    
    @TableField("duration_months")
    private Integer durationMonths;
    
    private String technology;
    private String achievement;
    private String description;
    
    @TableField("project_scale")
    private String projectScale;
    
    @TableField("team_size")
    private Integer teamSize;
    
    @TableField("created_time")
    private LocalDateTime createdTime;
    
    @TableField("updated_time")
    private LocalDateTime updatedTime;
    
    // 构造函数
    public ProjectExperience() {
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }
    
    public ProjectExperience(Long employeeId, String projectName, String role, Integer durationMonths, 
                           String technology, String achievement) {
        this();
        this.employeeId = employeeId;
        this.projectName = projectName;
        this.role = role;
        this.durationMonths = durationMonths;
        this.technology = technology;
        this.achievement = achievement;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public Integer getDurationMonths() { return durationMonths; }
    public void setDurationMonths(Integer durationMonths) { this.durationMonths = durationMonths; }
    
    public String getTechnology() { return technology; }
    public void setTechnology(String technology) { this.technology = technology; }
    
    public String getAchievement() { return achievement; }
    public void setAchievement(String achievement) { this.achievement = achievement; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getProjectScale() { return projectScale; }
    public void setProjectScale(String projectScale) { this.projectScale = projectScale; }
    
    public Integer getTeamSize() { return teamSize; }
    public void setTeamSize(Integer teamSize) { this.teamSize = teamSize; }
    
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    
    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }
}
