package com.orainge.wenwen;

import com.spring4all.mongodb.EnableMongoPlus;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableMongoPlus
@MapperScan("com.orainge.wenwen.mybatis.mapper") // 自动扫描包下的所有 Mapper
@EnableMongoRepositories("com.orainge.wenwen.mongo.entity") // 自动扫描 MongoDB 的实体类
@EnableAsync   //开启异步功能注解
@SpringBootApplication
public class WenwenApplication {
    public static void main(String[] args) {
        SpringApplication.run(WenwenApplication.class, args);
    }

}