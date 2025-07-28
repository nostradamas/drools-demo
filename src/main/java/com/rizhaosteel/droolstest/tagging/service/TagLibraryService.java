package com.rizhaosteel.droolstest.tagging.service;

import com.rizhaosteel.droolstest.tagging.model.Tag;
import com.rizhaosteel.droolstest.tagging.model.TagCategory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 标签库管理服务
 */
@Service
public class TagLibraryService {
    
    private final Map<String, Tag> tagLibrary = new HashMap<>();
    
    public TagLibraryService() {
        initializeTagLibrary();
    }
    
    /**
     * 初始化标签库
     */
    private void initializeTagLibrary() {
        // 能力素质类标签
        addTag(new Tag("TECH_EXPERT", "技术专家", "在特定技术领域有深入专业知识", TagCategory.TECHNICAL_ABILITY, 9, 0.9, "#FF6B6B"));
        addTag(new Tag("TECH_SENIOR", "高级技术人员", "技术能力强，经验丰富", TagCategory.TECHNICAL_ABILITY, 8, 0.8, "#4ECDC4"));
        addTag(new Tag("TECH_INTERMEDIATE", "中级技术人员", "技术能力中等，有一定经验", TagCategory.TECHNICAL_ABILITY, 6, 0.6, "#45B7D1"));
        addTag(new Tag("TECH_JUNIOR", "初级技术人员", "技术基础扎实，需要指导", TagCategory.TECHNICAL_ABILITY, 4, 0.4, "#96CEB4"));
        addTag(new Tag("FULLSTACK_DEV", "全栈开发者", "前后端技术都很熟练", TagCategory.TECHNICAL_ABILITY, 8, 0.8, "#FFEAA7"));
        addTag(new Tag("FRONTEND_EXPERT", "前端专家", "前端技术专家", TagCategory.TECHNICAL_ABILITY, 8, 0.8, "#DDA0DD"));
        addTag(new Tag("BACKEND_EXPERT", "后端专家", "后端技术专家", TagCategory.TECHNICAL_ABILITY, 8, 0.8, "#98D8C8"));
        
        addTag(new Tag("STRONG_LEADER", "强领导力", "具备优秀的领导和管理能力", TagCategory.LEADERSHIP, 9, 0.9, "#FF7675"));
        addTag(new Tag("POTENTIAL_LEADER", "潜在领导者", "具备领导潜质", TagCategory.LEADERSHIP, 7, 0.7, "#FD79A8"));
        addTag(new Tag("TEAM_PLAYER", "团队协作者", "善于团队合作", TagCategory.LEADERSHIP, 6, 0.6, "#FDCB6E"));
        
        addTag(new Tag("EXCELLENT_COMM", "沟通专家", "沟通能力出色", TagCategory.COMMUNICATION, 8, 0.8, "#6C5CE7"));
        addTag(new Tag("GOOD_COMM", "沟通良好", "沟通能力较好", TagCategory.COMMUNICATION, 6, 0.6, "#A29BFE"));
        
        addTag(new Tag("INNOVATOR", "创新者", "具备创新思维和能力", TagCategory.INNOVATION, 9, 0.9, "#00B894"));
        addTag(new Tag("CREATIVE_THINKER", "创意思考者", "思维活跃，有创意", TagCategory.INNOVATION, 7, 0.7, "#55A3FF"));
        
        // 履历类标签
        addTag(new Tag("VETERAN_10PLUS", "资深专家(10年+)", "工作经验10年以上", TagCategory.EXPERIENCE_LEVEL, 9, 0.9, "#E17055"));
        addTag(new Tag("SENIOR_5TO10", "高级人员(5-10年)", "工作经验5-10年", TagCategory.EXPERIENCE_LEVEL, 8, 0.8, "#F39C12"));
        addTag(new Tag("MID_3TO5", "中级人员(3-5年)", "工作经验3-5年", TagCategory.EXPERIENCE_LEVEL, 6, 0.6, "#F1C40F"));
        addTag(new Tag("JUNIOR_1TO3", "初级人员(1-3年)", "工作经验1-3年", TagCategory.EXPERIENCE_LEVEL, 4, 0.4, "#2ECC71"));
        addTag(new Tag("FRESH_GRAD", "应届毕业生", "工作经验不足1年", TagCategory.EXPERIENCE_LEVEL, 2, 0.2, "#3498DB"));
        
        addTag(new Tag("PHD_DEGREE", "博士学历", "具有博士学位", TagCategory.EDUCATION_BACKGROUND, 9, 0.9, "#9B59B6"));
        addTag(new Tag("MASTER_DEGREE", "硕士学历", "具有硕士学位", TagCategory.EDUCATION_BACKGROUND, 7, 0.7, "#8E44AD"));
        addTag(new Tag("BACHELOR_DEGREE", "本科学历", "具有本科学位", TagCategory.EDUCATION_BACKGROUND, 5, 0.5, "#3F51B5"));
        addTag(new Tag("TOP_UNIVERSITY", "名校毕业", "985/211等知名院校毕业", TagCategory.EDUCATION_BACKGROUND, 8, 0.8, "#E91E63"));
        
        addTag(new Tag("MULTI_INDUSTRY", "跨行业经验", "有多个行业工作经验", TagCategory.INDUSTRY_EXPERIENCE, 8, 0.8, "#FF9800"));
        addTag(new Tag("FINTECH_EXP", "金融科技经验", "有金融科技行业经验", TagCategory.INDUSTRY_EXPERIENCE, 7, 0.7, "#795548"));
        addTag(new Tag("ECOMMERCE_EXP", "电商经验", "有电商行业经验", TagCategory.INDUSTRY_EXPERIENCE, 7, 0.7, "#607D8B"));
        
        addTag(new Tag("LARGE_PROJECT", "大型项目经验", "参与过大型项目", TagCategory.PROJECT_EXPERIENCE, 8, 0.8, "#FF5722"));
        addTag(new Tag("STARTUP_EXP", "创业经验", "有创业或初创公司经验", TagCategory.PROJECT_EXPERIENCE, 8, 0.8, "#009688"));
        
        // 考评类标签
        addTag(new Tag("TOP_PERFORMER", "顶尖绩效", "绩效评级A+", TagCategory.PERFORMANCE_LEVEL, 10, 1.0, "#C0392B"));
        addTag(new Tag("HIGH_PERFORMER", "高绩效", "绩效评级A", TagCategory.PERFORMANCE_LEVEL, 9, 0.9, "#E74C3C"));
        addTag(new Tag("GOOD_PERFORMER", "良好绩效", "绩效评级B", TagCategory.PERFORMANCE_LEVEL, 7, 0.7, "#F39C12"));
        addTag(new Tag("AVERAGE_PERFORMER", "一般绩效", "绩效评级C", TagCategory.PERFORMANCE_LEVEL, 5, 0.5, "#F1C40F"));
        addTag(new Tag("NEEDS_IMPROVEMENT", "需要改进", "绩效评级D", TagCategory.PERFORMANCE_LEVEL, 3, 0.3, "#95A5A6"));
        
        addTag(new Tag("HIGH_POTENTIAL", "高潜力", "潜力评估为高", TagCategory.POTENTIAL_ASSESSMENT, 9, 0.9, "#8E44AD"));
        addTag(new Tag("MEDIUM_POTENTIAL", "中等潜力", "潜力评估为中等", TagCategory.POTENTIAL_ASSESSMENT, 6, 0.6, "#3498DB"));
        addTag(new Tag("RISING_STAR", "新星", "快速成长的员工", TagCategory.POTENTIAL_ASSESSMENT, 8, 0.8, "#E67E22"));
        
        addTag(new Tag("COMPETENT_ALL", "全面胜任", "各项胜任力都达标", TagCategory.COMPETENCY_RATING, 8, 0.8, "#27AE60"));
        addTag(new Tag("COMPETENT_PARTIAL", "部分胜任", "部分胜任力达标", TagCategory.COMPETENCY_RATING, 6, 0.6, "#F39C12"));
        
        addTag(new Tag("EXCELLENT_COLLAB", "协作优秀", "团队协作能力优秀", TagCategory.TEAM_COLLABORATION, 9, 0.9, "#16A085"));
        addTag(new Tag("GOOD_COLLAB", "协作良好", "团队协作能力良好", TagCategory.TEAM_COLLABORATION, 7, 0.7, "#1ABC9C"));
        
        addTag(new Tag("RAPID_GROWTH", "快速成长", "成长趋势向上", TagCategory.GROWTH_TREND, 8, 0.8, "#2ECC71"));
        addTag(new Tag("STABLE_GROWTH", "稳定成长", "成长趋势稳定", TagCategory.GROWTH_TREND, 6, 0.6, "#3498DB"));
        addTag(new Tag("SLOW_GROWTH", "成长缓慢", "成长趋势较慢", TagCategory.GROWTH_TREND, 4, 0.4, "#95A5A6"));
    }
    
    private void addTag(Tag tag) {
        tagLibrary.put(tag.getCode(), tag);
    }
    
    /**
     * 获取所有标签
     */
    public List<Tag> getAllTags() {
        return new ArrayList<>(tagLibrary.values());
    }
    
    /**
     * 根据编码获取标签
     */
    public Tag getTagByCode(String code) {
        return tagLibrary.get(code);
    }
    
    /**
     * 根据维度获取标签
     */
    public List<Tag> getTagsByDimension(String dimension) {
        return tagLibrary.values().stream()
                .filter(tag -> dimension.equals(tag.getDimension()) && tag.isActive())
                .sorted(Comparator.comparing(Tag::getPriority).reversed())
                .collect(Collectors.toList());
    }
    
    /**
     * 根据分类获取标签
     */
    public List<Tag> getTagsByCategory(TagCategory category) {
        return tagLibrary.values().stream()
                .filter(tag -> category.equals(tag.getCategory()) && tag.isActive())
                .sorted(Comparator.comparing(Tag::getPriority).reversed())
                .collect(Collectors.toList());
    }
    
    /**
     * 搜索标签
     */
    public List<Tag> searchTags(String keyword) {
        return tagLibrary.values().stream()
                .filter(tag -> tag.isActive() && 
                        (tag.getName().contains(keyword) || 
                         tag.getDescription().contains(keyword) ||
                         tag.getCode().contains(keyword)))
                .sorted(Comparator.comparing(Tag::getPriority).reversed())
                .collect(Collectors.toList());
    }
    
    /**
     * 添加新标签
     */
    public void addNewTag(Tag tag) {
        tagLibrary.put(tag.getCode(), tag);
    }
    
    /**
     * 更新标签
     */
    public void updateTag(Tag tag) {
        if (tagLibrary.containsKey(tag.getCode())) {
            tagLibrary.put(tag.getCode(), tag);
        }
    }
    
    /**
     * 删除标签（软删除）
     */
    public void deleteTag(String code) {
        Tag tag = tagLibrary.get(code);
        if (tag != null) {
            tag.setActive(false);
        }
    }
    
    /**
     * 获取标签统计信息
     */
    public Map<String, Object> getTagStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalTags = tagLibrary.values().stream().filter(Tag::isActive).count();
        stats.put("totalTags", totalTags);
        
        Map<String, Long> dimensionStats = tagLibrary.values().stream()
                .filter(Tag::isActive)
                .collect(Collectors.groupingBy(Tag::getDimension, Collectors.counting()));
        stats.put("dimensionStats", dimensionStats);
        
        Map<TagCategory, Long> categoryStats = tagLibrary.values().stream()
                .filter(Tag::isActive)
                .collect(Collectors.groupingBy(Tag::getCategory, Collectors.counting()));
        stats.put("categoryStats", categoryStats);
        
        return stats;
    }
}
