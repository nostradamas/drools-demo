package com.rizhaosteel.droolstest.tagging.model;

import java.time.LocalDateTime;

/**
 * 员工标签关联实体
 */
public class EmployeeTag {
    private Long id;
    private Long employeeId;
    private String tagCode;
    private String tagName;
    private TagCategory category;
    private String dimension;
    private double confidence; // 置信度 0.0-1.0
    private String source; // 标签来源：RULE_ENGINE, MANUAL, SYSTEM
    private String reason; // 打标签原因
    private LocalDateTime assignedTime; // 分配时间
    private LocalDateTime expireTime; // 过期时间
    private boolean isActive; // 是否有效
    
    public EmployeeTag() {}
    
    public EmployeeTag(Long employeeId, Tag tag, double confidence, String source, String reason) {
        this.employeeId = employeeId;
        this.tagCode = tag.getCode();
        this.tagName = tag.getName();
        this.category = tag.getCategory();
        this.dimension = tag.getDimension();
        this.confidence = confidence;
        this.source = source;
        this.reason = reason;
        this.assignedTime = LocalDateTime.now();
        this.isActive = true;
        
        // 根据标签类型设置过期时间
        if ("EVALUATION".equals(dimension)) {
            this.expireTime = LocalDateTime.now().plusMonths(6); // 考评类标签6个月过期
        } else if ("ABILITY".equals(dimension)) {
            this.expireTime = LocalDateTime.now().plusYears(1); // 能力类标签1年过期
        } else {
            this.expireTime = LocalDateTime.now().plusYears(2); // 履历类标签2年过期
        }
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
    
    public TagCategory getCategory() { return category; }
    public void setCategory(TagCategory category) { this.category = category; }
    
    public String getDimension() { return dimension; }
    public void setDimension(String dimension) { this.dimension = dimension; }
    
    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public LocalDateTime getAssignedTime() { return assignedTime; }
    public void setAssignedTime(LocalDateTime assignedTime) { this.assignedTime = assignedTime; }
    
    public LocalDateTime getExpireTime() { return expireTime; }
    public void setExpireTime(LocalDateTime expireTime) { this.expireTime = expireTime; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public boolean isExpired() {
        return expireTime != null && LocalDateTime.now().isAfter(expireTime);
    }
}
