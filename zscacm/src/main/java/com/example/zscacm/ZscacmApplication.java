package com.example.zscacm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
@MapperScan("com.example.zscacm.mapper")
@EnableScheduling
@EnableCaching//开启注解驱动的缓存管理
@EnableKafka
public class ZscacmApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZscacmApplication.class, args);
    }

}
