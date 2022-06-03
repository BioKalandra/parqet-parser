package com.jan.coin.parqetparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ParqetParserApplication {
    
    private static ApplicationContext applicationContext;
    
    public static void main(String[] args) {
        SpringApplication.run(ParqetParserApplication.class, args);
        checkBeansPresence("fileParser");
    }
    
    private static void checkBeansPresence(String... beans) {
        for (String beanName : beans) {
            System.out.println("Is " + beanName + " in ApplicationContext: " + applicationContext.containsBean(beanName));
        }
    }
    
}
