package com.db.cb.signalhandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration("proxyBeanMethods")
public class MainApp {

    public static void main(String[] args){
        SpringApplication.run(MainApp.class, args);
    }

}