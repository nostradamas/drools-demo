package com.rizhaosteel.droolstest.tagging.model;

/**
 * 标签分类枚举
 */
public enum TagCategory {
    // 能力素质类标签
    TECHNICAL_ABILITY("技术能力", "ABILITY"),
    SOFT_SKILL("软技能", "ABILITY"), 
    LEADERSHIP("领导力", "ABILITY"),
    COMMUNICATION("沟通能力", "ABILITY"),
    INNOVATION("创新能力", "ABILITY"),
    
    // 履历类标签
    EXPERIENCE_LEVEL("经验等级", "EXPERIENCE"),
    EDUCATION_BACKGROUND("教育背景", "EXPERIENCE"),
    INDUSTRY_EXPERIENCE("行业经验", "EXPERIENCE"),
    PROJECT_EXPERIENCE("项目经验", "EXPERIENCE"),
    CERTIFICATION("认证资质", "EXPERIENCE"),
    
    // 考评类标签
    PERFORMANCE_LEVEL("绩效等级", "EVALUATION"),
    POTENTIAL_ASSESSMENT("潜力评估", "EVALUATION"),
    COMPETENCY_RATING("胜任力评级", "EVALUATION"),
    TEAM_COLLABORATION("团队协作", "EVALUATION"),
    GROWTH_TREND("成长趋势", "EVALUATION");
    
    private final String displayName;
    private final String dimension;
    
    TagCategory(String displayName, String dimension) {
        this.displayName = displayName;
        this.dimension = dimension;
    }
    
    public String getDisplayName() { return displayName; }
    public String getDimension() { return dimension; }
}
