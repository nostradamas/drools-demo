package com.rizhaosteel.droolstest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rizhaosteel.droolstest.entity.EmployeeTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 员工标签关联Mapper
 */
@Mapper
public interface EmployeeTagMapper extends BaseMapper<EmployeeTag> {

    /**
     * 根据员工ID查询标签
     */
    @Select("SELECT * FROM employee_tag WHERE employee_id = #{employeeId} AND is_active = 1 ORDER BY assigned_time DESC")
    List<EmployeeTag> findByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * 根据标签编码查询员工
     */
    @Select("SELECT * FROM employee_tag WHERE tag_code = #{tagCode} AND is_active = 1 ORDER BY confidence DESC")
    List<EmployeeTag> findByTagCode(@Param("tagCode") String tagCode);

    /**
     * 根据员工ID和维度查询标签
     */
    @Select("SELECT * FROM employee_tag WHERE employee_id = #{employeeId} AND dimension = #{dimension} AND is_active = 1 ORDER BY confidence DESC")
    List<EmployeeTag> findByEmployeeIdAndDimension(@Param("employeeId") Long employeeId, @Param("dimension") String dimension);

    /**
     * 根据来源查询标签
     */
    @Select("SELECT * FROM employee_tag WHERE source = #{source} AND is_active = 1 ORDER BY assigned_time DESC")
    List<EmployeeTag> findBySource(@Param("source") String source);

    /**
     * 批量失效员工的某类标签
     */
    @Update("UPDATE employee_tag SET is_active = 0 WHERE employee_id = #{employeeId} AND source = #{source}")
    int deactivateByEmployeeIdAndSource(@Param("employeeId") Long employeeId, @Param("source") String source);

    /**
     * 统计员工标签数量
     */
    @Select("SELECT employee_id, COUNT(*) as tag_count FROM employee_tag WHERE is_active = 1 GROUP BY employee_id")
    List<Map<String, Object>> countTagsByEmployee();

    /**
     * 统计标签使用频率
     */
    @Select("SELECT tag_code, tag_name, COUNT(*) as usage_count FROM employee_tag WHERE is_active = 1 GROUP BY tag_code, tag_name ORDER BY usage_count DESC")
    List<Map<String, Object>> getTagUsageFrequency();

    /**
     * 按维度统计员工标签
     */
    @Select("SELECT dimension, COUNT(*) as count FROM employee_tag WHERE is_active = 1 GROUP BY dimension")
    List<Map<String, Object>> countByDimension();

    /**
     * 按来源统计员工标签
     */
    @Select("SELECT source, COUNT(*) as count FROM employee_tag WHERE is_active = 1 GROUP BY source")
    List<Map<String, Object>> countBySource();

    /**
     * 查询即将过期的标签
     */
    @Select("SELECT * FROM employee_tag WHERE is_active = 1 AND expire_time <= DATE_ADD(NOW(), INTERVAL #{days} DAY) ORDER BY expire_time ASC")
    List<EmployeeTag> findExpiringTags(@Param("days") Integer days);
}
