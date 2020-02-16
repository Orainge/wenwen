package com.orainge.wenwen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@MapperScan("com.orainge.wenwen.mapper") // 自动扫描包下的所有 Mapper
@EnableMongoRepositories("com.orainge.wenwen.model.mongo") // 自动扫描 MongoDB 的实体类
public class WenwenApplication {
    public static void main(String[] args) {
        SpringApplication.run(WenwenApplication.class, args);
    }
}