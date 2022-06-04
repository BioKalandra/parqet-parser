package com.jan.coin.parqetparser;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ParqetParserApplication {
    
    public static void main(String[] args) throws IOException {
        
        ApplicationContext applicationContext = SpringApplication.run(ParqetParserApplication.class, args);
        ProgramStarter beansOfType = applicationContext.getBean(ProgramStarter.class);
        beansOfType.startProcess();
        
    }
    
}
