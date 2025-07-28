package com.rizhaosteel.droolstest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Drools员工标签系统统一启动类
 * 整合员工匹配和标签管理功能
 * 
 * @author system
 * @version 1.0
 */
@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class DroolsEmployeeTaggingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DroolsEmployeeTaggingApplication.class, args);
        
        System.out.println("===========================================");
        System.out.println("Drools员工标签系统启动成功！");
        System.out.println("访问地址: http://localhost:8080");
        System.out.println("前端页面: http://localhost:8080/index.html");
        System.out.println("测试页面: http://localhost:8080/test.html");
        System.out.println("===========================================");
        System.out.println("主要功能模块:");
        System.out.println("1. 员工标签管理 - /api/enhanced-tagging");
        System.out.println("2. 规则管理 - /api/rule-management");
        System.out.println("3. 标签库管理 - /api/tag-library-management");
        System.out.println("4. 集成标签服务 - /api/integrated-tagging");
        System.out.println("5. 员工匹配 - /api/matching");
        System.out.println("===========================================");
    }
}
