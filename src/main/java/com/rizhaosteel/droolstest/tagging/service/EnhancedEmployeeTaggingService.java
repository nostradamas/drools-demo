package com.rizhaosteel.droolstest.tagging.service;

import com.rizhaosteel.droolstest.tagging.model.EnhancedEmployee;
import com.rizhaosteel.droolstest.tagging.model.EmployeeTag;
import com.rizhaosteel.droolstest.tagging.model.Tag;
import com.rizhaosteel.droolstest.tagging.model.TagCategory;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 增强的员工标签服务 - JDK 1.8兼容版本
 */
@Service
public class EnhancedEmployeeTaggingService {
    
    @Autowired
    private KieSession kieSession;
    
    @Autowired
    private TagLibraryService tagLibraryService;
    
    /**
     * 给员工打标签（全维度）
     */
    public EnhancedEmployee tagEmployee(EnhancedEmployee employee) {
        // 清理过期标签
        cleanExpiredTags(employee);
        
        // 清空当前标签（保留手动添加的标签）
        List<EmployeeTag> tagsToRemove = new ArrayList<EmployeeTag>();
        for (EmployeeTag tag : employee.getCurrentTags()) {
            if ("RULE_ENGINE".equals(tag.getSource())) {
                tagsToRemove.add(tag);
            }
        }
        employee.getCurrentTags().removeAll(tagsToRemove);
        
        // 设置全局变量
        kieSession.setGlobal("tagLibraryService", tagLibraryService);
        
        // 插入员工对象到规则引擎
        kieSession.insert(employee);
        
        // 执行标签规则
        kieSession.fireAllRules();
        
        // 清理会话
        kieSession.dispose();
        
        // 按维度和优先级排序标签
        sortTagsByPriorityAndDimension(employee);
        
        return employee;
    }
    
    /**
     * 批量给员工打标签
     */
    public List<EnhancedEmployee> batchTagEmployees(List<EnhancedEmployee> employees) {
        List<EnhancedEmployee> taggedEmployees = new ArrayList<EnhancedEmployee>();
        
        for (EnhancedEmployee employee : employees) {
            EnhancedEmployee taggedEmployee = tagEmployee(employee);
            taggedEmployees.add(taggedEmployee);
        }
        
        return taggedEmployees;
    }
    
    /**
     * 按维度获取员工标签
     */
    public Map<String, List<EmployeeTag>> getEmployeeTagsByDimension(EnhancedEmployee employee) {
        Map<String, List<EmployeeTag>> result = new HashMap<String, List<EmployeeTag>>();
        
        for (EmployeeTag tag : employee.getCurrentTags()) {
            if (tag.isActive() && !tag.isExpired()) {
                String dimension = tag.getDimension();
                if (!result.containsKey(dimension)) {
                    result.put(dimension, new ArrayList<EmployeeTag>());
                }
                result.get(dimension).add(tag);
            }
        }
        
        return result;
    }
    
    /**
     * 获取员工标签统计
     */
    public Map<String, Object> getEmployeeTagStatistics(EnhancedEmployee employee) {
        Map<String, Object> stats = new HashMap<String, Object>();
        
        List<EmployeeTag> activeTags = new ArrayList<EmployeeTag>();
        for (EmployeeTag tag : employee.getCurrentTags()) {
            if (tag.isActive() && !tag.isExpired()) {
                activeTags.add(tag);
            }
        }
        
        stats.put("totalTags", activeTags.size());
        
        // 按维度统计
        Map<String, Long> dimensionStats = new HashMap<String, Long>();
        for (EmployeeTag tag : activeTags) {
            String dimension = tag.getDimension();
            dimensionStats.put(dimension, dimensionStats.getOrDefault(dimension, 0L) + 1);
        }
        stats.put("dimensionStats", dimensionStats);
        
        // 按分类统计
        Map<TagCategory, Long> categoryStats = new HashMap<TagCategory, Long>();
        for (EmployeeTag tag : activeTags) {
            TagCategory category = tag.getCategory();
            categoryStats.put(category, categoryStats.getOrDefault(category, 0L) + 1);
        }
        stats.put("categoryStats", categoryStats);
        
        // 平均置信度
        double avgConfidence = 0.0;
        if (!activeTags.isEmpty()) {
            double sum = 0.0;
            for (EmployeeTag tag : activeTags) {
                sum += tag.getConfidence();
            }
            avgConfidence = sum / activeTags.size();
        }
        stats.put("averageConfidence", avgConfidence);
        
        return stats;
    }
    
    /**
     * 手动添加标签
     */
    public void addManualTag(Long employeeId, String tagCode, String reason) {
        Tag tag = tagLibraryService.getTagByCode(tagCode);
        if (tag != null) {
            EmployeeTag employeeTag = new EmployeeTag(employeeId, tag, 1.0, "MANUAL", reason);
            // 这里应该保存到数据库或员工对象中
            System.out.println("手动为员工 " + employeeId + " 添加标签：" + tag.getName());
        }
    }
    
    /**
     * 移除标签
     */
    public void removeTag(EnhancedEmployee employee, String tagCode) {
        List<EmployeeTag> tagsToRemove = new ArrayList<EmployeeTag>();
        for (EmployeeTag tag : employee.getCurrentTags()) {
            if (tagCode.equals(tag.getTagCode())) {
                tagsToRemove.add(tag);
            }
        }
        employee.getCurrentTags().removeAll(tagsToRemove);
    }
    
    /**
     * 更新标签置信度
     */
    public void updateTagConfidence(EnhancedEmployee employee, String tagCode, double confidence) {
        for (EmployeeTag tag : employee.getCurrentTags()) {
            if (tagCode.equals(tag.getTagCode())) {
                tag.setConfidence(confidence);
                break;
            }
        }
    }
    
    /**
     * 清理过期标签
     */
    private void cleanExpiredTags(EnhancedEmployee employee) {
        for (EmployeeTag tag : employee.getCurrentTags()) {
            if (tag.isExpired()) {
                tag.setActive(false);
                System.out.println("标签 " + tag.getTagName() + " 已过期，自动失效");
            }
        }
    }
    
    /**
     * 按维度和优先级排序标签
     */
    private void sortTagsByPriorityAndDimension(EnhancedEmployee employee) {
        Collections.sort(employee.getCurrentTags(), new Comparator<EmployeeTag>() {
            @Override
            public int compare(EmployeeTag t1, EmployeeTag t2) {
                // 先按维度排序
                int dimensionCompare = t1.getDimension().compareTo(t2.getDimension());
                if (dimensionCompare != 0) {
                    return dimensionCompare;
                }
                // 再按置信度降序排序
                return Double.compare(t2.getConfidence(), t1.getConfidence());
            }
        });
    }
    
    /**
     * 根据标签推荐培训计划
     */
    public List<String> recommendTrainingByTags(EnhancedEmployee employee) {
        List<String> recommendations = new ArrayList<String>();
        Set<String> uniqueRecommendations = new HashSet<String>();
        
        for (EmployeeTag tag : employee.getCurrentTags()) {
            if (tag.isActive() && !tag.isExpired()) {
                switch (tag.getTagCode()) {
                    case "TECH_JUNIOR":
                        uniqueRecommendations.add("技术基础强化培训");
                        break;
                    case "POTENTIAL_LEADER":
                        uniqueRecommendations.add("领导力发展培训");
                        break;
                    case "NEEDS_IMPROVEMENT":
                        uniqueRecommendations.add("绩效提升专项培训");
                        break;
                    case "GOOD_COMM":
                        uniqueRecommendations.add("高级沟通技巧培训");
                        break;
                }
            }
        }
        
        recommendations.addAll(uniqueRecommendations);
        return recommendations;
    }
    
    /**
     * 标签变更历史记录
     */
    public void recordTagChange(Long employeeId, String tagCode, String action, String reason) {
        // 这里应该记录到数据库中
        System.out.println("员工 " + employeeId + " 标签变更：" + action + " " + tagCode + "，原因：" + reason);
    }
}
