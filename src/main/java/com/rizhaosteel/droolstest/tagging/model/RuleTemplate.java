package com.rizhaosteel.droolstest.tagging.model;

import java.util.Map;
import java.util.HashMap;

/**
 * 规则模板枚举
 */
public enum RuleTemplate {
    
    EXPERIENCE_BASED("经验年限模板", "基于工作经验年限的标签规则"),
    SKILL_BASED("技能模板", "基于技能组合的标签规则"),
    PERFORMANCE_BASED("绩效模板", "基于绩效评分的标签规则"),
    EDUCATION_BASED("学历模板", "基于教育背景的标签规则"),
    LEADERSHIP_BASED("领导力模板", "基于领导力评分的标签规则"),
    COMPOSITE("复合条件模板", "基于多个条件组合的标签规则"),
    CUSTOM("自定义模板", "完全自定义的标签规则");
    
    private final String displayName;
    private final String description;
    
    RuleTemplate(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    
    /**
     * 获取模板的默认参数
     */
    public Map<String, Object> getDefaultParameters() {
        Map<String, Object> params = new HashMap<>();
        
        switch (this) {
            case EXPERIENCE_BASED:
                params.put("minYears", 0);
                params.put("maxYears", 50);
                params.put("operator", ">=");
                break;
            case SKILL_BASED:
                params.put("requiredSkills", new String[]{});
                params.put("skillCount", 1);
                params.put("matchType", "ANY"); // ANY, ALL
                break;
            case PERFORMANCE_BASED:
                params.put("minScore", 0);
                params.put("maxScore", 100);
                params.put("operator", ">=");
                break;
            case EDUCATION_BASED:
                params.put("educationLevels", new String[]{"本科", "硕士", "博士"});
                params.put("matchType", "ANY");
                break;
            case LEADERSHIP_BASED:
                params.put("minLeadershipScore", 0);
                params.put("minManagementYears", 0);
                params.put("minTeamSize", 0);
                break;
            case COMPOSITE:
                params.put("conditions", new HashMap<>());
                params.put("logicOperator", "AND"); // AND, OR
                break;
        }
        
        return params;
    }
}
