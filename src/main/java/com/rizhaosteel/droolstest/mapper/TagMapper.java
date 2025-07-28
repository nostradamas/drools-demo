package com.rizhaosteel.droolstest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rizhaosteel.droolstest.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 标签Mapper
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 根据维度查询标签
     */
    @Select("SELECT * FROM tag WHERE dimension = #{dimension} AND is_active = 1 ORDER BY priority DESC")
    List<Tag> findByDimension(@Param("dimension") String dimension);

    /**
     * 根据分类查询标签
     */
    @Select("SELECT * FROM tag WHERE category = #{category} AND is_active = 1 ORDER BY priority DESC")
    List<Tag> findByCategory(@Param("category") String category);

    /**
     * 搜索标签
     */
    @Select("SELECT * FROM tag WHERE (name LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%') OR code LIKE CONCAT('%', #{keyword}, '%')) AND is_active = 1 ORDER BY priority DESC")
    List<Tag> searchTags(@Param("keyword") String keyword);

    /**
     * 统计标签使用情况
     */
    @Select("SELECT t.code, t.name, COUNT(et.id) as usage_count FROM tag t LEFT JOIN employee_tag et ON t.code = et.tag_code AND et.is_active = 1 WHERE t.is_active = 1 GROUP BY t.code, t.name ORDER BY usage_count DESC")
    List<Map<String, Object>> getTagUsageStatistics();

    /**
     * 按维度统计标签数量
     */
    @Select("SELECT dimension, COUNT(*) as count FROM tag WHERE is_active = 1 GROUP BY dimension")
    List<Map<String, Object>> countByDimension();

    /**
     * 按分类统计标签数量
     */
    @Select("SELECT category, COUNT(*) as count FROM tag WHERE is_active = 1 GROUP BY category")
    List<Map<String, Object>> countByCategory();
}
