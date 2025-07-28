package com.rizhaosteel.droolstest.matching.model;

import java.util.List;
import java.util.ArrayList;

public class Employee {
    private Long id;
    private String name;
    private int age;
    private String education; // 学历：本科、硕士、博士
    private int workExperience; // 工作经验年数
    private List<String> skills; // 技能列表
    private String currentPosition; // 当前职位
    private double currentSalary; // 当前薪资
    private double expectedSalary; // 期望薪资
    private List<String> tags; // 标签列表
    private int performanceScore; // 绩效分数 (1-100)
    private boolean hasManagementExperience; // 是否有管理经验
    
    public Employee() {
        this.skills = new ArrayList<>();
        this.tags = new ArrayList<>();
    }
    
    public Employee(Long id, String name, int age, String education, 
                   int workExperience, List<String> skills, String currentPosition, 
                   double currentSalary, double expectedSalary, int performanceScore, 
                   boolean hasManagementExperience) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.education = education;
        this.workExperience = workExperience;
        this.skills = skills != null ? skills : new ArrayList<>();
        this.currentPosition = currentPosition;
        this.currentSalary = currentSalary;
        this.expectedSalary = expectedSalary;
        this.tags = new ArrayList<>();
        this.performanceScore = performanceScore;
        this.hasManagementExperience = hasManagementExperience;
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public int getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(int workExperience) {
        this.workExperience = workExperience;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public double getCurrentSalary() {
        return currentSalary;
    }

    public void setCurrentSalary(double currentSalary) {
        this.currentSalary = currentSalary;
    }

    public double getExpectedSalary() {
        return expectedSalary;
    }

    public void setExpectedSalary(double expectedSalary) {
        this.expectedSalary = expectedSalary;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getPerformanceScore() {
        return performanceScore;
    }

    public void setPerformanceScore(int performanceScore) {
        this.performanceScore = performanceScore;
    }

    public boolean isHasManagementExperience() { return hasManagementExperience; }
    public void setHasManagementExperience(boolean hasManagementExperience) { this.hasManagementExperience = hasManagementExperience; }
    
    public void addTag(String tag) {
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }
    
    public boolean hasSkill(String skill) {
        return this.skills.contains(skill);
    }
}