package com.rizhaosteel.droolstest.tagging.model;

import java.time.LocalDateTime;

/**
 * 标签实体
 */
public class Tag {
    private Long id;
    private String code; // 标签编码
    private String name; // 标签名称
    private String description; // 标签描述
    private TagCategory category; // 标签分类
    private String dimension; // 所属维度：ABILITY, EXPERIENCE, EVALUATION
    private int priority; // 优先级 1-10
    private double weight; // 权重 0.1-1.0
    private boolean isActive; // 是否启用
    private String color; // 标签颜色
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    
    public Tag() {}
    
    public Tag(String code, String name, String description, TagCategory category, 
               int priority, double weight, String color) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.category = category;
        this.dimension = category.getDimension();
        this.priority = priority;
        this.weight = weight;
        this.color = color;
        this.isActive = true;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public TagCategory getCategory() { return category; }
    public void setCategory(TagCategory category) { 
        this.category = category;
        this.dimension = category.getDimension();
    }
    
    public String getDimension() { return dimension; }
    public void setDimension(String dimension) { this.dimension = dimension; }
    
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    
    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }
}
