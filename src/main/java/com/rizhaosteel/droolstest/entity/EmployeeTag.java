package com.rizhaosteel.droolstest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

import java.time.LocalDateTime;

/**
 * 员工标签关联实体类
 */
@TableName("employee_tag")
public class EmployeeTag {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("employee_id")
    private Long employeeId;
    
    @TableField("tag_code")
    private String tagCode;
    
    @TableField("tag_name")
    private String tagName;
    
    private String category;
    private String dimension;
    private Double confidence;
    private String source;
    private String reason;
    
    @TableField("assigned_time")
    private LocalDateTime assignedTime;
    
    @TableField("expire_time")
    private LocalDateTime expireTime;
    
    @TableField("is_active")
    private Boolean isActive;
    
    @TableField("created_time")
    private LocalDateTime createdTime;
    
    @TableField("updated_time")
    private LocalDateTime updatedTime;
    
    // 构造函数
    public EmployeeTag() {
        this.isActive = true;
        this.assignedTime = LocalDateTime.now();
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }
    
    public EmployeeTag(Long employeeId, String tagCode, String tagName, String category, String dimension, 
                      Double confidence, String source, String reason) {
        this();
        this.employeeId = employeeId;
        this.tagCode = tagCode;
        this.tagName = tagName;
        this.category = category;
        this.dimension = dimension;
        this.confidence = confidence;
        this.source = source;
        this.reason = reason;
        
        // 根据维度设置过期时间
        if ("EVALUATION".equals(dimension)) {
            this.expireTime = LocalDateTime.now().plusMonths(6); // 考评类标签6个月过期
        } else if ("ABILITY".equals(dimension)) {
            this.expireTime = LocalDateTime.now().plusYears(1); // 能力类标签1年过期
        } else {
            this.expireTime = LocalDateTime.now().plusYears(2); // 履历类标签2年过期
        }
    }
    
    // 便利方法
    public boolean isExpired() {
        return expireTime != null && LocalDateTime.now().isAfter(expireTime);
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    
    public String getTagCode() { return tagCode; }
    public void setTagCode(String tagCode) { this.tagCode = tagCode; }
    
    public String getTagName() { return tagName; }
    public void setTagName(String tagName) { this.tagName = tagName; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getDimension() { return dimension; }
    public void setDimension(String dimension) { this.dimension = dimension; }
    
    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public LocalDateTime getAssignedTime() { return assignedTime; }
    public void setAssignedTime(LocalDateTime assignedTime) { this.assignedTime = assignedTime; }
    
    public LocalDateTime getExpireTime() { return expireTime; }
    public void setExpireTime(LocalDateTime expireTime) { this.expireTime = expireTime; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    
    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }
}
