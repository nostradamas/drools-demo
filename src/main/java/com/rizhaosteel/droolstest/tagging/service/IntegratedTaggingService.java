package com.rizhaosteel.droolstest.tagging.service;

import com.rizhaosteel.droolstest.tagging.model.EnhancedEmployee;
import com.rizhaosteel.droolstest.tagging.model.EmployeeTag;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 集成标签服务 - 使用动态规则引擎 - JDK 1.8兼容版本
 */
@Service
public class IntegratedTaggingService {
    
    @Autowired
    private DynamicRuleService dynamicRuleService;
    
    @Autowired
    private TagLibraryService tagLibraryService;
    
    /**
     * 使用动态规则给员工打标签
     */
    public EnhancedEmployee tagEmployeeWithDynamicRules(EnhancedEmployee employee) {
        // 清理过期标签
        cleanExpiredTags(employee);
        
        // 清空规则引擎生成的标签
        List<EmployeeTag> tagsToRemove = new ArrayList<EmployeeTag>();
        for (EmployeeTag tag : employee.getCurrentTags()) {
            if ("RULE_ENGINE".equals(tag.getSource())) {
                tagsToRemove.add(tag);
            }
        }
        employee.getCurrentTags().removeAll(tagsToRemove);
        
        // 创建新的KieSession
        KieSession session = dynamicRuleService.createKieSession();
        if (session == null) {
            System.err.println("无法创建KieSession，使用动态规则打标签失败");
            return employee;
        }
        
        try {
            // 插入员工对象
            session.insert(employee);
            
            // 执行所有规则
            int firedRules = session.fireAllRules();
            
            System.out.println("员工 " + employee.getName() + " 动态规则执行完成，触发了 " + firedRules + " 条规则");
            
            // 按维度和置信度排序标签
            sortTagsByDimensionAndConfidence(employee);
            
        } finally {
            session.dispose();
        }
        
        return employee;
    }
    
    /**
     * 批量使用动态规则打标签
     */
    public List<EnhancedEmployee> batchTagEmployeesWithDynamicRules(List<EnhancedEmployee> employees) {
        List<EnhancedEmployee> taggedEmployees = new ArrayList<EnhancedEmployee>();
        
        for (EnhancedEmployee employee : employees) {
            EnhancedEmployee taggedEmployee = tagEmployeeWithDynamicRules(employee);
            taggedEmployees.add(taggedEmployee);
        }
        
        return taggedEmployees;
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
     * 按维度和置信度排序标签
     */
    private void sortTagsByDimensionAndConfidence(EnhancedEmployee employee) {
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
}
