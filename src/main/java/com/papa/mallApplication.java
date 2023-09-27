package com.papa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class mallApplication {
    public static void main(String[] args) {

        ConfigurableApplicationContext context=SpringApplication.run(mallApplication.class,args);



    }
}