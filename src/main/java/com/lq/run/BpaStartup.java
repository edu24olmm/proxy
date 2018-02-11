package com.lq.run;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"com.lq.conf","com.lq.controller","com.lq.interceptor","com.lq.server"})
public class BpaStartup {

    public static void main(String[] args) {
        SpringApplication.run(BpaStartup.class, args);
        System.out.println("---------启动成功----------------");
    }
}
