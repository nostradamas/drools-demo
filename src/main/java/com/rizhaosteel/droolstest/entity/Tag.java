package com.rizhaosteel.droolstest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

import java.time.LocalDateTime;

/**
 * 标签实体类
 */
@TableName("tag")
public class Tag {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String code;
    private String name;
    private String description;
    private String category;
    private String dimension;
    private Integer priority;
    private Double weight;
    private String color;
    
    @TableField("is_active")
    private Boolean isActive;
    
    @TableField("created_time")
    private LocalDateTime createdTime;
    
    @TableField("updated_time")
    private LocalDateTime updatedTime;
    
    @TableField("created_by")
    private String createdBy;
    
    @TableField("updated_by")
    private String updatedBy;
    
    // 构造函数
    public Tag() {
        this.isActive = true;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }
    
    public Tag(String code, String name, String description, String category, String dimension, Integer priority, Double weight, String color) {
        this();
        this.code = code;
        this.name = name;
        this.description = description;
        this.category = category;
        this.dimension = dimension;
        this.priority = priority;
        this.weight = weight;
        this.color = color;
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
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getDimension() { return dimension; }
    public void setDimension(String dimension) { this.dimension = dimension; }
    
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    
    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
}
