package com.rizhaosteel.droolstest.matching.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
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
        
        // 加载规则文件
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + "employee-tagging-rules.drl"));
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + "position-matching-rules.drl"));
        
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        
        KieRepository kieRepository = kieServices.getRepository();
        return kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
    }
    
    @Bean
    public KieSession kieSession() {
        return kieContainer().newKieSession();
    }
}
