package com.rizhaosteel.droolstest.matching.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfig {

    private static final String RULES_PATH = "rules/";
    
    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        
        // 加载员工标签规则
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + "employee-tagging-rules.drl"));
        
        // 加载增强员工标签规则
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + "enhanced-employee-tagging-rules.drl"));
        
        // 加载职位匹配规则
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + "position-matching-rules.drl"));
        
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        
        if (kieBuilder.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
            throw new RuntimeException("Build Errors:\n" + kieBuilder.getResults().toString());
        }
        
        KieModule kieModule = kieBuilder.getKieModule();
        return kieServices.newKieContainer(kieModule.getReleaseId());
    }
    
    @Bean
    public KieSession kieSession() {
        return kieContainer().newKieSession();
    }
}
