package com.rizhaosteel.droolstest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

import java.time.LocalDateTime;

/**
 * 标签规则实体类
 */
@TableName("tag_rule")
public class TagRule {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("rule_code")
    private String ruleCode;
    
    @TableField("rule_name")
    private String ruleName;
    
    @TableField("tag_code")
    private String tagCode;
    
    @TableField("rule_content")
    private String ruleContent;
    
    @TableField("rule_template")
    private String ruleTemplate;
    
    private String conditions;
    private String description;
    private Integer priority;
    
    @TableField("is_active")
    private Boolean isActive;
    
    private String version;
    
    @TableField("created_by")
    private String createdBy;
    
    @TableField("updated_by")
    private String updatedBy;
    
    @TableField("created_time")
    private LocalDateTime createdTime;
    
    @TableField("updated_time")
    private LocalDateTime updatedTime;
    
    @TableField("last_executed_time")
    private LocalDateTime lastExecutedTime;
    
    @TableField("execution_count")
    private Long executionCount;
    
    @TableField("test_cases")
    private String testCases; // JSON格式存储
    
    // 构造函数
    public TagRule() {
        this.isActive = true;
        this.priority = 5;
        this.version = "1.0";
        this.executionCount = 0L;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }
    
    public TagRule(String ruleCode, String ruleName, String tagCode, String ruleTemplate) {
        this();
        this.ruleCode = ruleCode;
        this.ruleName = ruleName;
        this.tagCode = tagCode;
        this.ruleTemplate = ruleTemplate;
    }
    
    // 便利方法
    public void incrementExecutionCount() {
        this.executionCount++;
        this.lastExecutedTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }
    
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    
    public String getTagCode() { return tagCode; }
    public void setTagCode(String tagCode) { this.tagCode = tagCode; }
    
    public String getRuleContent() { return ruleContent; }
    public void setRuleContent(String ruleContent) { this.ruleContent = ruleContent; }
    
    public String getRuleTemplate() { return ruleTemplate; }
    public void setRuleTemplate(String ruleTemplate) { this.ruleTemplate = ruleTemplate; }
    
    public String getConditions() { return conditions; }
    public void setConditions(String conditions) { this.conditions = conditions; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    
    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }
    
    public LocalDateTime getLastExecutedTime() { return lastExecutedTime; }
    public void setLastExecutedTime(LocalDateTime lastExecutedTime) { this.lastExecutedTime = lastExecutedTime; }
    
    public Long getExecutionCount() { return executionCount; }
    public void setExecutionCount(Long executionCount) { this.executionCount = executionCount; }
    
    public String getTestCases() { return testCases; }
    public void setTestCases(String testCases) { this.testCases = testCases; }
}
