package com.rizhaosteel.droolstest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rizhaosteel.droolstest.entity.ProjectExperience;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 项目经验Mapper
 */
@Mapper
public interface ProjectExperienceMapper extends BaseMapper<ProjectExperience> {

    /**
     * 根据员工ID查询项目经验
     */
    @Select("SELECT * FROM project_experience WHERE employee_id = #{employeeId} ORDER BY start_date DESC")
    List<ProjectExperience> findByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * 根据技术栈查询项目经验
     */
    @Select("SELECT * FROM project_experience WHERE technology LIKE CONCAT('%', #{technology}, '%') ORDER BY start_date DESC")
    List<ProjectExperience> findByTechnology(@Param("technology") String technology);

    /**
     * 根据项目角色查询项目经验
     */
    @Select("SELECT * FROM project_experience WHERE role = #{role} ORDER BY start_date DESC")
    List<ProjectExperience> findByRole(@Param("role") String role);

    /**
     * 统计员工项目数量
     */
    @Select("SELECT employee_id, COUNT(*) as project_count FROM project_experience GROUP BY employee_id")
    List<Map<String, Object>> countProjectsByEmployee();

    /**
     * 统计技术栈使用频率
     */
    @Select("SELECT technology, COUNT(*) as usage_count FROM project_experience WHERE technology IS NOT NULL GROUP BY technology ORDER BY usage_count DESC")
    List<Map<String, Object>> getTechnologyUsageStatistics();

    /**
     * 统计项目角色分布
     */
    @Select("SELECT role, COUNT(*) as count FROM project_experience WHERE role IS NOT NULL GROUP BY role ORDER BY count DESC")
    List<Map<String, Object>> getRoleDistribution();
}
