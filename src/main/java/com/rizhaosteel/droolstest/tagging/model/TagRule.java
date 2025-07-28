package com.rizhaosteel.droolstest.tagging.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * 标签规则实体
 */
public class TagRule {
    private Long id;
    private String ruleCode; // 规则编码
    private String ruleName; // 规则名称
    private String tagCode; // 关联的标签编码
    private String ruleContent; // 规则内容(DRL格式)
    private String ruleTemplate; // 规则模板类型
    private String conditions; // 条件配置(JSON格式)
    private String description; // 规则描述
    private int priority; // 规则优先级
    private boolean isActive; // 是否启用
    private String version; // 规则版本
    private String createdBy; // 创建人
    private String updatedBy; // 更新人
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private LocalDateTime lastExecutedTime; // 最后执行时间
    private long executionCount; // 执行次数
    private List<String> testCases; // 测试用例
    
    public TagRule() {
        this.testCases = new ArrayList<>();
        this.isActive = true;
        this.priority = 5;
        this.version = "1.0";
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
    
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
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
    
    public long getExecutionCount() { return executionCount; }
    public void setExecutionCount(long executionCount) { this.executionCount = executionCount; }
    
    public List<String> getTestCases() { return testCases; }
    public void setTestCases(List<String> testCases) { this.testCases = testCases; }
    
    public void incrementExecutionCount() {
        this.executionCount++;
        this.lastExecutedTime = LocalDateTime.now();
    }
}
