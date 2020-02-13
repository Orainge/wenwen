package com.orainge.wenwen;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;

//@SpringBootApplication(scanBasePackages = {"com.orainge.wenwen.controller", "com.orainge.wenwen.service.*",
//        "com.orainge.wenwen.mapper"})
@SpringBootApplication
@MapperScan(basePackages = "com.orainge.wenwen.mapper") //MyBatis: 自动扫描包下的所有 Mapper
public class WenwenApplication {
    public static void main(String[] args) {
        SpringApplication.run(WenwenApplication.class, args);
    }
}