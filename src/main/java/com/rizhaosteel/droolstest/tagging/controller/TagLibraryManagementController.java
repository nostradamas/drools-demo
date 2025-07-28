package com.rizhaosteel.droolstest.tagging.controller;

import com.rizhaosteel.droolstest.tagging.model.Tag;
import com.rizhaosteel.droolstest.tagging.model.TagCategory;
import com.rizhaosteel.droolstest.tagging.service.TagLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 标签库管理控制器
 */
@RestController
@RequestMapping("/api/tag-library-management")
@CrossOrigin(origins = "*")
public class TagLibraryManagementController {
    
    @Autowired
    private TagLibraryService tagLibraryService;
    
    /**
     * 获取所有标签
     */
    @GetMapping("/tags")
    public List<Tag> getAllTags() {
        return tagLibraryService.getAllTags();
    }
    
    /**
     * 根据ID获取标签
     */
    @GetMapping("/tags/{code}")
    public Tag getTagByCode(@PathVariable String code) {
        return tagLibraryService.getTagByCode(code);
    }
    
    /**
     * 根据维度获取标签
     */
    @GetMapping("/tags/dimension/{dimension}")
    public List<Tag> getTagsByDimension(@PathVariable String dimension) {
        return tagLibraryService.getTagsByDimension(dimension);
    }
    
    /**
     * 根据分类获取标签
     */
    @GetMapping("/tags/category/{category}")
    public List<Tag> getTagsByCategory(@PathVariable TagCategory category) {
        return tagLibraryService.getTagsByCategory(category);
    }
    
    /**
     * 搜索标签
     */
    @GetMapping("/tags/search")
    public List<Tag> searchTags(@RequestParam String keyword) {
        return tagLibraryService.searchTags(keyword);
    }
    
    /**
     * 添加新标签
     */
    @PostMapping("/tags")
    public ApiResponse addTag(@RequestBody CreateTagRequest request) {
        try {
            Tag tag = new Tag(
                request.getCode(),
                request.getName(),
                request.getDescription(),
                request.getCategory(),
                request.getPriority(),
                request.getWeight(),
                request.getColor()
            );
            
            tagLibraryService.addNewTag(tag);
            
            return new ApiResponse(true, "标签添加成功", tag);
        } catch (Exception e) {
            return new ApiResponse(false, "标签添加失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 更新标签
     */
    @PutMapping("/tags/{code}")
    public ApiResponse updateTag(@PathVariable String code, @RequestBody UpdateTagRequest request) {
        try {
            Tag existingTag = tagLibraryService.getTagByCode(code);
            if (existingTag == null) {
                return new ApiResponse(false, "标签不存在", null);
            }
            
            // 更新标签属性
            if (request.getName() != null) existingTag.setName(request.getName());
            if (request.getDescription() != null) existingTag.setDescription(request.getDescription());
            if (request.getCategory() != null) existingTag.setCategory(request.getCategory());
            if (request.getPriority() != null) existingTag.setPriority(request.getPriority());
            if (request.getWeight() != null) existingTag.setWeight(request.getWeight());
            if (request.getColor() != null) existingTag.setColor(request.getColor());
            if (request.getActive() != null) existingTag.setActive(request.getActive());
            
            existingTag.setUpdatedTime(LocalDateTime.now());
            
            tagLibraryService.updateTag(existingTag);
            
            return new ApiResponse(true, "标签更新成功", existingTag);
        } catch (Exception e) {
            return new ApiResponse(false, "标签更新失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 删除标签（软删除）
     */
    @DeleteMapping("/tags/{code}")
    public ApiResponse deleteTag(@PathVariable String code) {
        try {
            Tag tag = tagLibraryService.getTagByCode(code);
            if (tag == null) {
                return new ApiResponse(false, "标签不存在", null);
            }
            
            tagLibraryService.deleteTag(code);
            
            return new ApiResponse(true, "标签删除成功", null);
        } catch (Exception e) {
            return new ApiResponse(false, "标签删除失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 批量操作标签
     */
    @PostMapping("/tags/batch")
    public ApiResponse batchOperateTags(@RequestBody BatchTagOperationRequest request) {
        try {
            int successCount = 0;
            int failCount = 0;
            
            for (String tagCode : request.getTagCodes()) {
                try {
                    switch (request.getOperation()) {
                        case "activate":
                            Tag tag = tagLibraryService.getTagByCode(tagCode);
                            if (tag != null) {
                                tag.setActive(true);
                                tagLibraryService.updateTag(tag);
                                successCount++;
                            }
                            break;
                        case "deactivate":
                            tag = tagLibraryService.getTagByCode(tagCode);
                            if (tag != null) {
                                tag.setActive(false);
                                tagLibraryService.updateTag(tag);
                                successCount++;
                            }
                            break;
                        case "delete":
                            tagLibraryService.deleteTag(tagCode);
                            successCount++;
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
     * 获取标签统计信息
     */
    @GetMapping("/statistics")
    public Map<String, Object> getTagStatistics() {
        return tagLibraryService.getTagStatistics();
    }
    
    /**
     * 获取所有标签分类
     */
    @GetMapping("/categories")
    public TagCategory[] getAllCategories() {
        return TagCategory.values();
    }
    
    /**
     * 导出标签库
     */
    @GetMapping("/export")
    public TagLibraryExport exportTagLibrary() {
        TagLibraryExport export = new TagLibraryExport();
        export.setTags(tagLibraryService.getAllTags());
        export.setStatistics(tagLibraryService.getTagStatistics());
        export.setExportTime(LocalDateTime.now());
        return export;
    }
    
    /**
     * 导入标签库
     */
    @PostMapping("/import")
    public ApiResponse importTagLibrary(@RequestBody TagLibraryImport importData) {
        try {
            int importedCount = 0;
            int skippedCount = 0;
            
            for (Tag tag : importData.getTags()) {
                Tag existingTag = tagLibraryService.getTagByCode(tag.getCode());
                if (existingTag == null) {
                    tagLibraryService.addNewTag(tag);
                    importedCount++;
                } else {
                    if (importData.isOverwriteExisting()) {
                        tagLibraryService.updateTag(tag);
                        importedCount++;
                    } else {
                        skippedCount++;
                    }
                }
            }
            
            return new ApiResponse(true, 
                String.format("导入完成，导入: %d, 跳过: %d", importedCount, skippedCount),
                Map.of("imported", importedCount, "skipped", skippedCount));
                
        } catch (Exception e) {
            return new ApiResponse(false, "导入失败: " + e.getMessage(), null);
        }
    }
    
    // 内部类定义
    public static class CreateTagRequest {
        private String code;
        private String name;
        private String description;
        private TagCategory category;
        private int priority;
        private double weight;
        private String color;
        
        // Getters and Setters
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public TagCategory getCategory() { return category; }
        public void setCategory(TagCategory category) { this.category = category; }
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }
        public double getWeight() { return weight; }
        public void setWeight(double weight) { this.weight = weight; }
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
    }
    
    public static class UpdateTagRequest {
        private String name;
        private String description;
        private TagCategory category;
        private Integer priority;
        private Double weight;
        private String color;
        private Boolean active;
        
        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public TagCategory getCategory() { return category; }
        public void setCategory(TagCategory category) { this.category = category; }
        public Integer getPriority() { return priority; }
        public void setPriority(Integer priority) { this.priority = priority; }
        public Double getWeight() { return weight; }
        public void setWeight(Double weight) { this.weight = weight; }
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }
    }
    
    public static class BatchTagOperationRequest {
        private List<String> tagCodes;
        private String operation; // activate, deactivate, delete
        
        public List<String> getTagCodes() { return tagCodes; }
        public void setTagCodes(List<String> tagCodes) { this.tagCodes = tagCodes; }
        public String getOperation() { return operation; }
        public void setOperation(String operation) { this.operation = operation; }
    }
    
    public static class TagLibraryExport {
        private List<Tag> tags;
        private Map<String, Object> statistics;
        private LocalDateTime exportTime;
        
        public List<Tag> getTags() { return tags; }
        public void setTags(List<Tag> tags) { this.tags = tags; }
        public Map<String, Object> getStatistics() { return statistics; }
        public void setStatistics(Map<String, Object> statistics) { this.statistics = statistics; }
        public LocalDateTime getExportTime() { return exportTime; }
        public void setExportTime(LocalDateTime exportTime) { this.exportTime = exportTime; }
    }
    
    public static class TagLibraryImport {
        private List<Tag> tags;
        private boolean overwriteExisting;
        
        public List<Tag> getTags() { return tags; }
        public void setTags(List<Tag> tags) { this.tags = tags; }
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
