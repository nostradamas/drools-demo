package com.rizhaosteel.droolstest.matching.service;

import com.rizhaosteel.droolstest.matching.model.Employee;
import com.rizhaosteel.droolstest.matching.model.Position;
import com.rizhaosteel.droolstest.matching.model.MatchingResult;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 员工匹配服务 - JDK 1.8兼容版本
 */
@Service
public class EmployeeMatchingService {
    
    @Autowired
    private KieSession kieSession;
    
    /**
     * 给员工打标签
     */
    public Employee tagEmployee(Employee employee) {
        // 清空现有标签
        employee.getTags().clear();
        
        // 插入员工对象到规则引擎
        kieSession.insert(employee);
        
        // 执行标签规则
        kieSession.fireAllRules();
        
        // 清理会话
        kieSession.dispose();
        
        return employee;
    }
    
    /**
     * 员工与职位匹配
     */
    public MatchingResult matchEmployeeToPosition(Employee employee, Position position) {
        // 先给员工打标签
        Employee taggedEmployee = tagEmployee(employee);
        
        // 创建匹配结果
        MatchingResult result = new MatchingResult();
        result.setEmployee(taggedEmployee);
        result.setPosition(position);
        
        // 计算匹配度
        double matchScore = calculateMatchScore(taggedEmployee, position);
        result.setMatchScore(matchScore);
        
        // 生成匹配原因
        List<String> reasons = generateMatchReasons(taggedEmployee, position);
        result.setMatchReasons(reasons);
        
        // 判断是否推荐
        result.setRecommended(matchScore >= 0.6);
        
        return result;
    }
    
    /**
     * 批量匹配员工到职位
     */
    public List<MatchingResult> matchEmployeesToPosition(List<Employee> employees, Position position) {
        List<MatchingResult> results = new ArrayList<MatchingResult>();
        
        for (Employee employee : employees) {
            MatchingResult result = matchEmployeeToPosition(employee, position);
            results.add(result);
        }
        
        return results;
    }
    
    /**
     * 计算匹配度
     */
    private double calculateMatchScore(Employee employee, Position position) {
        double score = 0.0;
        double totalWeight = 0.0;
        
        // 技能匹配 (权重: 0.4)
        double skillScore = calculateSkillMatch(employee.getSkills(), position.getRequiredSkills(), position.getPreferredSkills());
        score += skillScore * 0.4;
        totalWeight += 0.4;
        
        // 经验匹配 (权重: 0.3)
        double experienceScore = calculateExperienceMatch(employee.getExperience(), position.getMinExperience(), position.getMaxExperience());
        score += experienceScore * 0.3;
        totalWeight += 0.3;
        
        // 学历匹配 (权重: 0.2)
        double educationScore = calculateEducationMatch(employee.getEducation(), position.getRequiredEducation());
        score += educationScore * 0.2;
        totalWeight += 0.2;
        
        // 薪资匹配 (权重: 0.1)
        double salaryScore = calculateSalaryMatch(employee.getExpectedSalary(), position.getMinSalary(), position.getMaxSalary());
        score += salaryScore * 0.1;
        totalWeight += 0.1;
        
        return totalWeight > 0 ? score / totalWeight : 0.0;
    }
    
    /**
     * 计算技能匹配度
     */
    private double calculateSkillMatch(List<String> employeeSkills, List<String> requiredSkills, List<String> preferredSkills) {
        if (employeeSkills == null || employeeSkills.isEmpty()) {
            return 0.0;
        }
        
        int requiredMatches = 0;
        int preferredMatches = 0;
        
        // 检查必需技能匹配
        if (requiredSkills != null) {
            for (String skill : requiredSkills) {
                if (employeeSkills.contains(skill)) {
                    requiredMatches++;
                }
            }
        }
        
        // 检查优选技能匹配
        if (preferredSkills != null) {
            for (String skill : preferredSkills) {
                if (employeeSkills.contains(skill)) {
                    preferredMatches++;
                }
            }
        }
        
        double requiredScore = requiredSkills != null && !requiredSkills.isEmpty() ? 
            (double) requiredMatches / requiredSkills.size() : 1.0;
        double preferredScore = preferredSkills != null && !preferredSkills.isEmpty() ? 
            (double) preferredMatches / preferredSkills.size() : 0.0;
        
        return requiredScore * 0.7 + preferredScore * 0.3;
    }
    
    /**
     * 计算经验匹配度
     */
    private double calculateExperienceMatch(int employeeExperience, int minExperience, int maxExperience) {
        if (employeeExperience < minExperience) {
            return Math.max(0.0, 1.0 - (double)(minExperience - employeeExperience) / minExperience);
        } else if (employeeExperience > maxExperience) {
            return Math.max(0.5, 1.0 - (double)(employeeExperience - maxExperience) / maxExperience);
        } else {
            return 1.0;
        }
    }
    
    /**
     * 计算学历匹配度
     */
    private double calculateEducationMatch(String employeeEducation, String requiredEducation) {
        if (employeeEducation == null || requiredEducation == null) {
            return 0.5;
        }
        
        List<String> educationLevels = Arrays.asList("高中", "大专", "本科", "硕士", "博士");
        int employeeLevel = educationLevels.indexOf(employeeEducation);
        int requiredLevel = educationLevels.indexOf(requiredEducation);
        
        if (employeeLevel == -1 || requiredLevel == -1) {
            return employeeEducation.equals(requiredEducation) ? 1.0 : 0.5;
        }
        
        if (employeeLevel >= requiredLevel) {
            return 1.0;
        } else {
            return Math.max(0.0, 1.0 - (double)(requiredLevel - employeeLevel) / educationLevels.size());
        }
    }
    
    /**
     * 计算薪资匹配度
     */
    private double calculateSalaryMatch(int expectedSalary, int minSalary, int maxSalary) {
        if (expectedSalary >= minSalary && expectedSalary <= maxSalary) {
            return 1.0;
        } else if (expectedSalary < minSalary) {
            return 0.8; // 期望薪资低于最低薪资，仍有一定匹配度
        } else {
            // 期望薪资高于最高薪资，根据超出程度计算匹配度
            double excess = (double)(expectedSalary - maxSalary) / maxSalary;
            return Math.max(0.0, 1.0 - excess);
        }
    }
    
    /**
     * 生成匹配原因
     */
    private List<String> generateMatchReasons(Employee employee, Position position) {
        List<String> reasons = new ArrayList<String>();
        
        // 技能匹配原因
        if (employee.getSkills() != null && position.getRequiredSkills() != null) {
            List<String> matchedSkills = new ArrayList<String>();
            for (String skill : position.getRequiredSkills()) {
                if (employee.getSkills().contains(skill)) {
                    matchedSkills.add(skill);
                }
            }
            if (!matchedSkills.isEmpty()) {
                reasons.add("具备必需技能: " + String.join(", ", matchedSkills));
            }
        }
        
        // 经验匹配原因
        if (employee.getExperience() >= position.getMinExperience()) {
            reasons.add("工作经验符合要求: " + employee.getExperience() + "年");
        }
        
        // 学历匹配原因
        if (employee.getEducation() != null && employee.getEducation().equals(position.getRequiredEducation())) {
            reasons.add("学历匹配: " + employee.getEducation());
        }
        
        // 薪资匹配原因
        if (employee.getExpectedSalary() >= position.getMinSalary() && 
            employee.getExpectedSalary() <= position.getMaxSalary()) {
            reasons.add("薪资期望在合理范围内");
        }
        
        // 标签匹配原因
        if (employee.getTags() != null && !employee.getTags().isEmpty()) {
            reasons.add("员工标签: " + String.join(", ", employee.getTags()));
        }
        
        return reasons;
    }
}
