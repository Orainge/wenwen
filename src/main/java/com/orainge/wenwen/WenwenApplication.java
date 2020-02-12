package com.orainge.wenwen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.orainge.mapper") //MyBatis: 自动扫描包下的所有 Mapper
public class WenwenApplication {
    public static void main(String[] args) {
        SpringApplication.run(WenwenApplication.class, args);
    }
}
