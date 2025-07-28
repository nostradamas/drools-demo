package com.rizhaosteel.droolstest.tagging.controller;

import com.rizhaosteel.droolstest.tagging.model.TagRule;
import com.rizhaosteel.droolstest.tagging.model.RuleTemplate;
import com.rizhaosteel.droolstest.tagging.service.DynamicRuleService;
import com.rizhaosteel.droolstest.tagging.service.RuleTemplateService;
import com.rizhaosteel.droolstest.tagging.model.EnhancedEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 规则管理控制器
 */
@RestController
@RequestMapping("/api/rule-management")
@CrossOrigin(origins = "*")
public class RuleManagementController {
    
    @Autowired
    private DynamicRuleService dynamicRuleService;
    
    @Autowired
    private RuleTemplateService ruleTemplateService;
    
    /**
     * 获取所有规则
     */
    @GetMapping("/rules")
    public List<TagRule> getAllRules() {
        return dynamicRuleService.getAllRules();
    }
    
    /**
     * 根据编码获取规则
     */
    @GetMapping("/rules/{ruleCode}")
    public TagRule getRule(@PathVariable String ruleCode) {
        return dynamicRuleService.getRule(ruleCode);
    }
    
    /**
     * 根据标签获取规则
     */
    @GetMapping("/rules/tag/{tagCode}")
    public List<TagRule> getRulesByTag(@PathVariable String tagCode) {
        return dynamicRuleService.getRulesByTag(tagCode);
    }
    
    /**
     * 添加新规则
     */
    @PostMapping("/rules")
    public ApiResponse addRule(@RequestBody CreateRuleRequest request) {
        try {
            TagRule rule = new TagRule();
            rule.setRuleCode(request.getRuleCode());
            rule.setRuleName(request.getRuleName());
            rule.setTagCode(request.getTagCode());
            rule.setRuleTemplate(request.getRuleTemplate());
            rule.setConditions(request.getConditions());
            rule.setDescription(request.getDescription());
            rule.setPriority(request.getPriority());
            rule.setRuleContent(request.getRuleContent());
            rule.setCreatedBy(request.getCreatedBy());
            
            boolean success = dynamicRuleService.addOrUpdateRule(rule);
            
            if (success) {
                return new ApiResponse(true, "规则添加成功", rule);
            } else {
                return new ApiResponse(false, "规则添加失败", null);
            }
        } catch (Exception e) {
            return new ApiResponse(false, "规则添加失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 更新规则
     */
    @PutMapping("/rules/{ruleCode}")
    public ApiResponse updateRule(@PathVariable String ruleCode, @RequestBody UpdateRuleRequest request) {
        try {
            TagRule existingRule = dynamicRuleService.getRule(ruleCode);
            if (existingRule == null) {
                return new ApiResponse(false, "规则不存在", null);
            }
            
            // 更新规则属性
            if (request.getRuleName() != null) existingRule.setRuleName(request.getRuleName());
            if (request.getTagCode() != null) existingRule.setTagCode(request.getTagCode());
            if (request.getRuleTemplate() != null) existingRule.setRuleTemplate(request.getRuleTemplate());
            if (request.getConditions() != null) existingRule.setConditions(request.getConditions());
            if (request.getDescription() != null) existingRule.setDescription(request.getDescription());
            if (request.getPriority() != null) existingRule.setPriority(request.getPriority());
            if (request.getRuleContent() != null) existingRule.setRuleContent(request.getRuleContent());
            if (request.getActive() != null) existingRule.setActive(request.getActive());
            
            existingRule.setUpdatedBy(request.getUpdatedBy());
            existingRule.setUpdatedTime(LocalDateTime.now());
            
            // 如果更新了模板或条件，需要重新生成规则内容
            if (request.getRuleTemplate() != null || request.getConditions() != null) {
                existingRule.setRuleContent(null); // 清空，让系统重新生成
            }
            
            boolean success = dynamicRuleService.addOrUpdateRule(existingRule);
            
            if (success) {
                return new ApiResponse(true, "规则更新成功", existingRule);
            } else {
                return new ApiResponse(false, "规则更新失败", null);
            }
        } catch (Exception e) {
            return new ApiResponse(false, "规则更新失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 删除规则
     */
    @DeleteMapping("/rules/{ruleCode}")
    public ApiResponse deleteRule(@PathVariable String ruleCode) {
        try {
            boolean success = dynamicRuleService.deleteRule(ruleCode);
            
            if (success) {
                return new ApiResponse(true, "规则删除成功", null);
            } else {
                return new ApiResponse(false, "规则删除失败", null);
            }
        } catch (Exception e) {
            return new ApiResponse(false, "规则删除失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 启用/禁用规则
     */
    @PostMapping("/rules/{ruleCode}/toggle")
    public ApiResponse toggleRule(@PathVariable String ruleCode, @RequestBody ToggleRuleRequest request) {
        try {
            boolean success = dynamicRuleService.toggleRule(ruleCode, request.isActive());
            
            if (success) {
                String action = request.isActive() ? "启用" : "禁用";
                return new ApiResponse(true, "规则" + action + "成功", null);
            } else {
                return new ApiResponse(false, "规则状态切换失败", null);
            }
        } catch (Exception e) {
            return new ApiResponse(false, "规则状态切换失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 测试规则
     */
    @PostMapping("/rules/{ruleCode}/test")
    public Map<String, Object> testRule(@PathVariable String ruleCode, @RequestBody EnhancedEmployee testEmployee) {
        return dynamicRuleService.testRule(ruleCode, testEmployee);
    }
    
    /**
     * 预览规则内容
     */
    @PostMapping("/rules/preview")
    public ApiResponse previewRule(@RequestBody PreviewRuleRequest request) {
        try {
            TagRule tempRule = new TagRule();
            tempRule.setRuleCode(request.getRuleCode());
            tempRule.setRuleName(request.getRuleName());
            tempRule.setTagCode(request.getTagCode());
            tempRule.setRuleTemplate(request.getRuleTemplate());
            tempRule.setConditions(request.getConditions());
            tempRule.setPriority(request.getPriority());
            
            String generatedContent = ruleTemplateService.generateRuleContent(tempRule);
            
            return new ApiResponse(true, "规则预览生成成功", 
                Map.of("ruleContent", generatedContent, "isValid", ruleTemplateService.validateRule(generatedContent)));
                
        } catch (Exception e) {
            return new ApiResponse(false, "规则预览生成失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 验证规则语法
     */
    @PostMapping("/rules/validate")
    public ApiResponse validateRule(@RequestBody ValidateRuleRequest request) {
        try {
            boolean isValid = ruleTemplateService.validateRule(request.getRuleContent());
            
            return new ApiResponse(true, "规则验证完成", 
                Map.of("isValid", isValid, "message", isValid ? "规则语法正确" : "规则语法错误"));
                
        } catch (Exception e) {
            return new ApiResponse(false, "规则验证失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 获取所有规则模板
     */
    @GetMapping("/templates")
    public RuleTemplate[] getAllTemplates() {
        return ruleTemplateService.getAllTemplates();
    }
    
    /**
     * 获取模板默认参数
     */
    @GetMapping("/templates/{template}/parameters")
    public Map<String, Object> getTemplateParameters(@PathVariable RuleTemplate template) {
        return template.getDefaultParameters();
    }
    
    /**
     * 获取规则统计信息
     */
    @GetMapping("/statistics")
    public Map<String, Object> getRuleStatistics() {
        return dynamicRuleService.getRuleStatistics();
    }
    
    /**
     * 批量操作规则
     */
    @PostMapping("/rules/batch")
    public ApiResponse batchOperateRules(@RequestBody BatchRuleOperationRequest request) {
        try {
            int successCount = 0;
            int failCount = 0;
            
            for (String ruleCode : request.getRuleCodes()) {
                try {
                    switch (request.getOperation()) {
                        case "activate":
                            if (dynamicRuleService.toggleRule(ruleCode, true)) {
                                successCount++;
                            } else {
                                failCount++;
                            }
                            break;
                        case "deactivate":
                            if (dynamicRuleService.toggleRule(ruleCode, false)) {
                                successCount++;
                            } else {
                                failCount++;
                            }
                            break;
                        case "delete":
                            if (dynamicRuleService.deleteRule(ruleCode)) {
                                successCount++;
                            } else {
                                failCount++;
                            }
                            break;
                        default:
                            failCount++;
                    }
                } catch (Exception e) {
                    failCount++;
                }
            }
            
            return new ApiResponse(true, 
                String.format("批量操作完成，成功: %d, 失败: %d", successCount, failCount), 
                Map.of("success", successCount, "failed", failCount));
                
        } catch (Exception e) {
            return new ApiResponse(false, "批量操作失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 初始化默认规则
     */
    @PostMapping("/rules/initialize")
    public ApiResponse initializeDefaultRules() {
        try {
            dynamicRuleService.initializeDefaultRules();
            return new ApiResponse(true, "默认规则初始化成功", null);
        } catch (Exception e) {
            return new ApiResponse(false, "默认规则初始化失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 导出规则配置
     */
    @GetMapping("/export")
    public RuleExport exportRules() {
        RuleExport export = new RuleExport();
        export.setRules(dynamicRuleService.getAllRules());
        export.setStatistics(dynamicRuleService.getRuleStatistics());
        export.setExportTime(LocalDateTime.now());
        return export;
    }
    
    /**
     * 导入规则配置
     */
    @PostMapping("/import")
    public ApiResponse importRules(@RequestBody RuleImport importData) {
        try {
            int importedCount = 0;
            int skippedCount = 0;
            int failedCount = 0;
            
            for (TagRule rule : importData.getRules()) {
                try {
                    TagRule existingRule = dynamicRuleService.getRule(rule.getRuleCode());
                    if (existingRule == null) {
                        if (dynamicRuleService.addOrUpdateRule(rule)) {
                            importedCount++;
                        } else {
                            failedCount++;
                        }
                    } else {
                        if (importData.isOverwriteExisting()) {
                            if (dynamicRuleService.addOrUpdateRule(rule)) {
                                importedCount++;
                            } else {
                                failedCount++;
                            }
                        } else {
                            skippedCount++;
                        }
                    }
                } catch (Exception e) {
                    failedCount++;
                }
            }
            
            return new ApiResponse(true, 
                String.format("导入完成，导入: %d, 跳过: %d, 失败: %d", importedCount, skippedCount, failedCount),
                Map.of("imported", importedCount, "skipped", skippedCount, "failed", failedCount));
                
        } catch (Exception e) {
            return new ApiResponse(false, "导入失败: " + e.getMessage(), null);
        }
    }
    
    // 内部类定义
    public static class CreateRuleRequest {
        private String ruleCode;
        private String ruleName;
        private String tagCode;
        private String ruleTemplate;
        private String conditions;
        private String description;
        private int priority = 5;
        private String ruleContent;
        private String createdBy;
        
        // Getters and Setters
        public String getRuleCode() { return ruleCode; }
        public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }
        public String getRuleName() { return ruleName; }
        public void setRuleName(String ruleName) { this.ruleName = ruleName; }
        public String getTagCode() { return tagCode; }
        public void setTagCode(String tagCode) { this.tagCode = tagCode; }
        public String getRuleTemplate() { return ruleTemplate; }
        public void setRuleTemplate(String ruleTemplate) { this.ruleTemplate = ruleTemplate; }
        public String getConditions() { return conditions; }
        public void setConditions(String conditions) { this.conditions = conditions; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }
        public String getRuleContent() { return ruleContent; }
        public void setRuleContent(String ruleContent) { this.ruleContent = ruleContent; }
        public String getCreatedBy() { return createdBy; }
        public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    }
    
    public static class UpdateRuleRequest {
        private String ruleName;
        private String tagCode;
        private String ruleTemplate;
        private String conditions;
        private String description;
        private Integer priority;
        private String ruleContent;
        private Boolean active;
        private String updatedBy;
        
        // Getters and Setters
        public String getRuleName() { return ruleName; }
        public void setRuleName(String ruleName) { this.ruleName = ruleName; }
        public String getTagCode() { return tagCode; }
        public void setTagCode(String tagCode) { this.tagCode = tagCode; }
        public String getRuleTemplate() { return ruleTemplate; }
        public void setRuleTemplate(String ruleTemplate) { this.ruleTemplate = ruleTemplate; }
        public String getConditions() { return conditions; }
        public void setConditions(String conditions) { this.conditions = conditions; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Integer getPriority() { return priority; }
        public void setPriority(Integer priority) { this.priority = priority; }
        public String getRuleContent() { return ruleContent; }
        public void setRuleContent(String ruleContent) { this.ruleContent = ruleContent; }
        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }
        public String getUpdatedBy() { return updatedBy; }
        public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    }
    
    public static class ToggleRuleRequest {
        private boolean active;
        
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
    }
    
    public static class PreviewRuleRequest {
        private String ruleCode;
        private String ruleName;
        private String tagCode;
        private String ruleTemplate;
        private String conditions;
        private int priority = 5;
        
        // Getters and Setters
        public String getRuleCode() { return ruleCode; }
        public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }
        public String getRuleName() { return ruleName; }
        public void setRuleName(String ruleName) { this.ruleName = ruleName; }
        public String getTagCode() { return tagCode; }
        public void setTagCode(String tagCode) { this.tagCode = tagCode; }
        public String getRuleTemplate() { return ruleTemplate; }
        public void setRuleTemplate(String ruleTemplate) { this.ruleTemplate = ruleTemplate; }
        public String getConditions() { return conditions; }
        public void setConditions(String conditions) { this.conditions = conditions; }
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }
    }
    
    public static class ValidateRuleRequest {
        private String ruleContent;
        
        public String getRuleContent() { return ruleContent; }
        public void setRuleContent(String ruleContent) { this.ruleContent = ruleContent; }
    }
    
    public static class BatchRuleOperationRequest {
        private List<String> ruleCodes;
        private String operation; // activate, deactivate, delete
        
        public List<String> getRuleCodes() { return ruleCodes; }
        public void setRuleCodes(List<String> ruleCodes) { this.ruleCodes = ruleCodes; }
        public String getOperation() { return operation; }
        public void setOperation(String operation) { this.operation = operation; }
    }
    
    public static class RuleExport {
        private List<TagRule> rules;
        private Map<String, Object> statistics;
        private LocalDateTime exportTime;
        
        public List<TagRule> getRules() { return rules; }
        public void setRules(List<TagRule> rules) { this.rules = rules; }
        public Map<String, Object> getStatistics() { return statistics; }
        public void setStatistics(Map<String, Object> statistics) { this.statistics = statistics; }
        public LocalDateTime getExportTime() { return exportTime; }
        public void setExportTime(LocalDateTime exportTime) { this.exportTime = exportTime; }
    }
    
    public static class RuleImport {
        private List<TagRule> rules;
        private boolean overwriteExisting;
        
        public List<TagRule> getRules() { return rules; }
        public void setRules(List<TagRule> rules) { this.rules = rules; }
        public boolean isOverwriteExisting() { return overwriteExisting; }
        public void setOverwriteExisting(boolean overwriteExisting) { this.overwriteExisting = overwriteExisting; }
    }
    
    public static class ApiResponse {
        private boolean success;
        private String message;
        private Object data;
        
        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
    }
}
