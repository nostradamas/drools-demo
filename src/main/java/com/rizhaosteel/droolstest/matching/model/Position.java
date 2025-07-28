package com.rizhaosteel.droolstest.matching.model;

import java.util.List;
import java.util.ArrayList;

public class Position {
    private Long id;
    private String title;
    private String department;
    private List<String> requiredSkills; // 必需技能
    private List<String> preferredSkills; // 优选技能
    private int minExperience; // 最少工作经验
    private int maxExperience; // 最多工作经验
    private String minEducation; // 最低学历要求
    private double minSalary; // 最低薪资
    private double maxSalary; // 最高薪资
    private String level; // 职位级别：初级、中级、高级、专家
    private boolean requiresManagement; // 是否需要管理经验
    
    public Position() {
        this.requiredSkills = new ArrayList<>();
        this.preferredSkills = new ArrayList<>();
    }
    
    public Position(Long id, String title, String department, List<String> requiredSkills,
                   List<String> preferredSkills, int minExperience, int maxExperience,
                   String minEducation, double minSalary, double maxSalary, String level,
                   boolean requiresManagement) {
        this.id = id;
        this.title = title;
        this.department = department;
        this.requiredSkills = requiredSkills != null ? requiredSkills : new ArrayList<>();
        this.preferredSkills = preferredSkills != null ? preferredSkills : new ArrayList<>();
        this.minExperience = minExperience;
        this.maxExperience = maxExperience;
        this.minEducation = minEducation;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.level = level;
        this.requiresManagement = requiresManagement;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }
    
    public List<String> getPreferredSkills() { return preferredSkills; }
    public void setPreferredSkills(List<String> preferredSkills) { this.preferredSkills = preferredSkills; }
    
    public int getMinExperience() { return minExperience; }
    public void setMinExperience(int minExperience) { this.minExperience = minExperience; }
    
    public int getMaxExperience() { return maxExperience; }
    public void setMaxExperience(int maxExperience) { this.maxExperience = maxExperience; }
    
    public String getMinEducation() { return minEducation; }
    public void setMinEducation(String minEducation) { this.minEducation = minEducation; }
    
    public double getMinSalary() { return minSalary; }
    public void setMinSalary(double minSalary) { this.minSalary = minSalary; }
    
    public double getMaxSalary() { return maxSalary; }
    public void setMaxSalary(double maxSalary) { this.maxSalary = maxSalary; }
    
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    
    public boolean isRequiresManagement() { return requiresManagement; }
    public void setRequiresManagement(boolean requiresManagement) { this.requiresManagement = requiresManagement; }
}
