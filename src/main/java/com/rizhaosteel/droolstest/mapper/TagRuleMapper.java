package com.rizhaosteel.droolstest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rizhaosteel.droolstest.entity.TagRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 标签规则Mapper
 */
@Mapper
public interface TagRuleMapper extends BaseMapper<TagRule> {

    /**
     * 根据标签编码查询规则
     */
    @Select("SELECT * FROM tag_rule WHERE tag_code = #{tagCode} AND is_active = 1 ORDER BY priority DESC")
    List<TagRule> findByTagCode(@Param("tagCode") String tagCode);

    /**
     * 根据规则模板查询规则
     */
    @Select("SELECT * FROM tag_rule WHERE rule_template = #{template} AND is_active = 1 ORDER BY priority DESC")
    List<TagRule> findByTemplate(@Param("template") String template);

    /**
     * 查询活跃规则
     */
    @Select("SELECT * FROM tag_rule WHERE is_active = 1 ORDER BY priority DESC")
    List<TagRule> findActiveRules();

    /**
     * 更新规则执行统计
     */
    @Update("UPDATE tag_rule SET execution_count = execution_count + 1, last_executed_time = NOW() WHERE rule_code = #{ruleCode}")
    int updateExecutionCount(@Param("ruleCode") String ruleCode);

    /**
     * 按模板统计规则数量
     */
    @Select("SELECT rule_template, COUNT(*) as count FROM tag_rule WHERE is_active = 1 GROUP BY rule_template")
    List<Map<String, Object>> countByTemplate();

    /**
     * 按标签统计规则数量
     */
    @Select("SELECT tag_code, COUNT(*) as count FROM tag_rule WHERE is_active = 1 GROUP BY tag_code")
    List<Map<String, Object>> countByTag();

    /**
     * 获取规则执行统计
     */
    @Select("SELECT rule_code, rule_name, execution_count, last_executed_time FROM tag_rule WHERE is_active = 1 ORDER BY execution_count DESC")
    List<Map<String, Object>> getExecutionStatistics();

    /**
     * 查询最近创建的规则
     */
    @Select("SELECT * FROM tag_rule WHERE is_active = 1 ORDER BY created_time DESC LIMIT #{limit}")
    List<TagRule> findRecentRules(@Param("limit") Integer limit);
}
