package com.example.zscacm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.zscacm.mapper")
public class ZscacmApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZscacmApplication.class, args);
    }

}
