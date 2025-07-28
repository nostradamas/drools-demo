package com.rizhaosteel.droolstest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rizhaosteel.droolstest.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 员工信息Mapper
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    /**
     * 根据部门查询员工
     */
    @Select("SELECT * FROM employee WHERE department = #{department} AND is_deleted = 0")
    List<Employee> findByDepartment(@Param("department") String department);

    /**
     * 根据职位查询员工
     */
    @Select("SELECT * FROM employee WHERE position = #{position} AND is_deleted = 0")
    List<Employee> findByPosition(@Param("position") String position);

    /**
     * 查询高绩效员工
     */
    @Select("SELECT * FROM employee WHERE current_performance_score >= #{minScore} AND is_deleted = 0")
    List<Employee> findHighPerformers(@Param("minScore") Integer minScore);

    /**
     * 根据工作年限查询员工
     */
    @Select("SELECT * FROM employee WHERE total_work_years >= #{minYears} AND total_work_years <= #{maxYears} AND is_deleted = 0")
    List<Employee> findByWorkYears(@Param("minYears") Integer minYears, @Param("maxYears") Integer maxYears);

    /**
     * 根据学历查询员工
     */
    @Select("SELECT * FROM employee WHERE education = #{education} AND is_deleted = 0")
    List<Employee> findByEducation(@Param("education") String education);

    /**
     * 统计员工数量按部门
     */
    @Select("SELECT department, COUNT(*) as count FROM employee WHERE is_deleted = 0 GROUP BY department")
    List<Map<String, Object>> countByDepartment();

    /**
     * 统计员工数量按职位
     */
    @Select("SELECT position, COUNT(*) as count FROM employee WHERE is_deleted = 0 GROUP BY position")
    List<Map<String, Object>> countByPosition();

    /**
     * 统计员工数量按绩效等级
     */
    @Select("SELECT performance_level, COUNT(*) as count FROM employee WHERE is_deleted = 0 GROUP BY performance_level")
    List<Map<String, Object>> countByPerformanceLevel();
}
