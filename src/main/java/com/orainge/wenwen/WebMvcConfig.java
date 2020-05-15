package com.orainge.wenwen;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.orainge.wenwen.util.WebsiteSettings;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String staticPath = "/html/static/"; // TODO 暂时更改静态文件目录，("classpath:/static/");
        // 设置静态文件访问路径
        // 第一个为从根开始的前缀，例如 /static/** 表示在访问文件时 http://根地址/static/文件具体路径
        // 第二个方法设置资源路径
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:" + staticPath);
        registry.addResourceHandler("/img/**").addResourceLocations("file:" + WebsiteSettings.IMAGE_DISK_PATH + "/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:" + staticPath + "favicon.ico");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 使用 FastJson
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter(); // 创建fastJson消息转换器
        FastJsonConfig fastJsonConfig = new FastJsonConfig(); // 创建配置类
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss"); //设置格式化的时间格式

        // 修改配置返回内容的过滤
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.DisableCircularReferenceDetect
//                SerializerFeature.WriteMapNullValue, // 当字段为null时，输出为key:null
//                SerializerFeature.WriteNullStringAsEmpty // 当String为null时，输出为key:""
        );

        // 处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(new MediaType("application", "json", StandardCharsets.UTF_8));
        fastConverter.setSupportedMediaTypes(fastMediaTypes);

        fastConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastConverter);// 将 fastjson 添加到视图消息转换器列表内
    }

    /* 解决跨域问题 */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // **代表所有路径
                .allowedOrigins("*") // allowOrigin指可以通过的ip，*代表所有，可以使用指定的ip，多个的话可以用逗号分隔，默认为*
                .allowedMethods("GET", "POST", "HEAD", "PUT", "DELETE") // 指请求方式 默认为*
                .allowCredentials(true) // 支持证书，默认为true
                .maxAge(3600) // 最大过期时间，默认为-1
                .allowedHeaders("*");
    }
}