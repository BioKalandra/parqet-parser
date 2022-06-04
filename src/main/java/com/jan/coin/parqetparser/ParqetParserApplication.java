package com.jan.coin.parqetparser;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ParqetParserApplication {
    
    private static ApplicationContext applicationContext;
    
    public static void main(String[] args) throws IOException {
        
        applicationContext = SpringApplication.run(ParqetParserApplication.class, args);
        ProgramStarter bean = (ProgramStarter) applicationContext.getBean("parser");
        bean.startProcess();
        
    }
    
}
