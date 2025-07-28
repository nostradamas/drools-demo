package com.rizhaosteel.droolstest.matching.controller;

import com.rizhaosteel.droolstest.matching.model.Employee;
import com.rizhaosteel.droolstest.matching.model.Position;
import com.rizhaosteel.droolstest.matching.model.MatchingResult;
import com.rizhaosteel.droolstest.matching.service.EmployeeMatchingService;
import com.rizhaosteel.droolstest.common.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/matching")
@CrossOrigin(origins = "*")
public class EmployeeMatchingController {

    @Autowired
    private EmployeeMatchingService matchingService;

    /**
     * 给员工打标签
     */
    @PostMapping("/tag-employee")
    public Employee tagEmployee(@RequestBody Employee employee) {
        return matchingService.tagEmployee(employee);
    }

    /**
     * 单个员工与单个职位匹配
     */
    @PostMapping("/single")
    public ApiResponse<MatchingResult> matchSingle(@RequestBody MatchRequest request) {
        try {
            MatchingResult result = matchingService.matchEmployeeToPosition(
                request.getEmployee(), request.getPosition());
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("匹配失败: " + e.getMessage());
        }
    }

    /**
     * 单个员工与多个职位匹配
     */
    @PostMapping("/employee-to-positions")
    public ApiResponse<List<MatchingResult>> matchEmployeeToPositions(@RequestBody EmployeeToPositionsRequest request) {
        try {
            List<MatchingResult> results = matchingService.matchEmployeeToPositions(
                request.getEmployee(), request.getPositions());
            return ApiResponse.success(results);
        } catch (Exception e) {
            return ApiResponse.error("匹配失败: " + e.getMessage());
        }
    }

    /**
     * 多个员工与单个职位匹配
     */
    @PostMapping("/employees-to-position")
    public ApiResponse<List<MatchingResult>> matchEmployeesToPosition(@RequestBody EmployeesToPositionRequest request) {
        try {
            List<MatchingResult> results = matchingService.matchEmployeesToPosition(
                request.getEmployees(), request.getPosition());
            return ApiResponse.success(results);
        } catch (Exception e) {
            return ApiResponse.error("匹配失败: " + e.getMessage());
        }
    }

    /**
     * 批量匹配
     */
    @PostMapping("/batch")
    public ApiResponse<List<MatchingResult>> batchMatch(@RequestBody BatchMatchRequest request) {
        try {
            List<MatchingResult> results = matchingService.batchMatch(
                request.getEmployees(), request.getPositions());
            return ApiResponse.success(results);
        } catch (Exception e) {
            return ApiResponse.error("批量匹配失败: " + e.getMessage());
        }
    }

    /**
     * 获取推荐匹配
     */
    @PostMapping("/recommended")
    public ApiResponse<List<MatchingResult>> getRecommendedMatches(@RequestBody BatchMatchRequest request) {
        try {
            List<MatchingResult> allResults = matchingService.batchMatch(
                request.getEmployees(), request.getPositions());
            List<MatchingResult> recommended = matchingService.getRecommendedMatches(allResults);
            return ApiResponse.success(recommended);
        } catch (Exception e) {
            return ApiResponse.error("获取推荐匹配失败: " + e.getMessage());
        }
    }

    /**
     * 测试匹配功能
     */
    @GetMapping("/test")
    public ApiResponse<MatchingResult> testMatching() {
        try {
            // 创建测试员工
            Employee employee = new Employee();
            employee.setId(1L);
            employee.setName("张三");
            employee.setAge(28);
            employee.setEducation("本科");
            employee.setWorkExperience(3);
            employee.setSkills(Arrays.asList("Java", "Spring", "MySQL", "JavaScript"));
            employee.setCurrentPosition("Java开发工程师");
            employee.setCurrentSalary(12000);
            employee.setExpectedSalary(15000);
            employee.setPerformanceScore(85);
            employee.setHasManagementExperience(false);

            // 创建测试职位
            Position position = new Position();
            position.setId(1L);
            position.setTitle("高级Java开发工程师");
            position.setDepartment("技术部");
            position.setRequiredSkills(Arrays.asList("Java", "Spring", "MySQL"));
            position.setPreferredSkills(Arrays.asList("Redis", "Docker", "Kubernetes"));
            position.setMinExperience(2);
            position.setMaxExperience(5);
            position.setMinEducation("本科");
            position.setMinSalary(12000);
            position.setMaxSalary(18000);
            position.setLevel("中级");
            position.setRequiresManagement(false);

            MatchingResult result = matchingService.matchEmployeeToPosition(employee, position);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("测试失败: " + e.getMessage());
        }
    }

    /**
     * 员工与职位匹配
     */
    @PostMapping("/match")
    public MatchingResult matchEmployeeToPosition(@RequestBody MatchRequest request) {
        return matchingService.matchEmployeeToPosition(request.getEmployee(), request.getPosition());
    }

    /**
     * 批量匹配员工到职位
     */
    @PostMapping("/batch-match")
    public List<MatchingResult> batchMatchLegacy(@RequestBody BatchMatchRequest request) {
        return matchingService.matchEmployeesToPosition(request.getEmployees(), request.getPosition());
    }

    /**
     * 获取测试数据
     */
    @GetMapping("/test-data")
    public TestData getTestData() {
        return createTestData();
    }

    /**
     * 完整测试示例
     */
    @GetMapping("/demo")
    public DemoResult runDemo() {
        TestData testData = createTestData();
        DemoResult result = new DemoResult();

        // 1. 给所有员工打标签
        for (Employee employee : testData.getEmployees()) {
            Employee taggedEmployee = matchingService.tagEmployee(employee);
            result.getTaggedEmployees().add(taggedEmployee);
        }

        // 2. 匹配员工到高级Java开发工程师职位
        Position javaPosition = testData.getPositions().get(0); // 高级Java开发工程师
        List<MatchingResult> matchResults = matchingService.matchEmployeesToPosition(
            testData.getEmployees(), javaPosition);
        result.setMatchingResults(matchResults);

        return result;
    }

    private TestData createTestData() {
        TestData testData = new TestData();

        // 创建测试员工
        Employee emp1 = new Employee(1L, "张三", 28, "本科", 5,
            Arrays.asList("Java", "Spring", "MySQL", "微服务"), "Java开发工程师",
            25000, 85, true);

        Employee emp2 = new Employee(2L, "李四", 25, "硕士", 3,
            Arrays.asList("JavaScript", "React", "Vue", "CSS"), "前端开发工程师",
            20000, 78, false);

        Employee emp3 = new Employee(3L, "王五", 32, "本科", 8,
            Arrays.asList("Java", "Python", "数据库", "架构设计"), "技术经理",
            35000, 92, true);

        Employee emp4 = new Employee(4L, "赵六", 24, "本科", 1,
            Arrays.asList("Java", "Spring"), "初级开发工程师",
            12000, 65, false);

        Employee emp5 = new Employee(5L, "钱七", 30, "博士", 6,
            Arrays.asList("Java", "Python", "机器学习", "数据分析"), "算法工程师",
            40000, 95, false);

        testData.setEmployees(Arrays.asList(emp1, emp2, emp3, emp4, emp5));

        // 创建测试职位
        Position pos1 = new Position(1L, "高级Java开发工程师", "技术部",
            Arrays.asList("Java", "Spring", "MySQL"),
            Arrays.asList("微服务", "Redis", "消息队列"),
            3, 8, "本科", 20000, 30000, "高级", false);

        Position pos2 = new Position(2L, "前端架构师", "技术部",
            Arrays.asList("JavaScript", "React", "Vue"),
            Arrays.asList("TypeScript", "Webpack", "性能优化"),
            5, 10, "本科", 25000, 35000, "专家", false);

        Position pos3 = new Position(3L, "技术总监", "技术部",
            Arrays.asList("Java", "架构设计", "团队管理"),
            Arrays.asList("微服务", "分布式系统", "技术规划"),
            8, 15, "本科", 40000, 60000, "专家", true);

        testData.setPositions(Arrays.asList(pos1, pos2, pos3));

        return testData;
    }

    // 内部类定义
    public static class MatchRequest {
        private Employee employee;
        private Position position;

        public Employee getEmployee() { return employee; }
        public void setEmployee(Employee employee) { this.employee = employee; }
        public Position getPosition() { return position; }
        public void setPosition(Position position) { this.position = position; }
    }

    public static class EmployeeToPositionsRequest {
        private Employee employee;
        private List<Position> positions;

        public Employee getEmployee() { return employee; }
        public void setEmployee(Employee employee) { this.employee = employee; }
        public List<Position> getPositions() { return positions; }
        public void setPositions(List<Position> positions) { this.positions = positions; }
    }

    public static class EmployeesToPositionRequest {
        private List<Employee> employees;
        private Position position;

        public List<Employee> getEmployees() { return employees; }
        public void setEmployees(List<Employee> employees) { this.employees = employees; }
        public Position getPosition() { return position; }
        public void setPosition(Position position) { this.position = position; }
    }

    public static class BatchMatchRequest {
        private List<Employee> employees;
        private List<Position> positions;

        public List<Employee> getEmployees() { return employees; }
        public void setEmployees(List<Employee> employees) { this.employees = employees; }
        public List<Position> getPositions() { return positions; }
        public void setPositions(List<Position> positions) { this.positions = positions; }
    }

    public static class TestData {
        private List<Employee> employees;
        private List<Position> positions;

        public List<Employee> getEmployees() { return employees; }
        public void setEmployees(List<Employee> employees) { this.employees = employees; }
        public List<Position> getPositions() { return positions; }
        public void setPositions(List<Position> positions) { this.positions = positions; }
    }

    public static class DemoResult {
        private List<Employee> taggedEmployees = new java.util.ArrayList<>();
        private List<MatchingResult> matchingResults;

        public List<Employee> getTaggedEmployees() { return taggedEmployees; }
        public void setTaggedEmployees(List<Employee> taggedEmployees) { this.taggedEmployees = taggedEmployees; }
        public List<MatchingResult> getMatchingResults() { return matchingResults; }
        public void setMatchingResults(List<MatchingResult> matchingResults) { this.matchingResults = matchingResults; }
    }
}
