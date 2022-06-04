package com.jan.coin.parqetparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ParqetParserApplication {
    
    public static void main(String[] args) throws Exception {
        
        ApplicationContext applicationContext = SpringApplication.run(ParqetParserApplication.class, args);
        ProgramStarter beansOfType = applicationContext.getBean(ProgramStarter.class);
        beansOfType.startProcess();
        
    }
    
}
