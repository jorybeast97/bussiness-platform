package com.fanhao.businessplatform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.fanhao.businessplatform.mapper")
@SpringBootApplication
public class BusinessPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusinessPlatformApplication.class, args);
    }

}
