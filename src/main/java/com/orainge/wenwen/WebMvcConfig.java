package com.orainge.wenwen;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Component
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 设置静态文件访问路径
        // 第一个为从根开始的前缀，例如 /static/** 表示在访问文件时 http://根地址/static/文件具体路径
        // 第二个方法设置资源路径
        // TODO 暂时更改静态文件目录，方便在 VS code 写前端页面 registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/html/static/");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 使用 FastJson
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter(); // 创建fastJson消息转换器
        FastJsonConfig fastJsonConfig = new FastJsonConfig(); // 创建配置类
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss"); //设置格式化的时间格式

        // 修改配置返回内容的过滤
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty
        );

        // 处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);

        fastConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastConverter);// 将 fastjson 添加到视图消息转换器列表内
    }
}