package com.atguigu.publisher.gmallpublisher;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.atguigu.publisher.gmallpublisher.mapper")
public class GmallPublisherApplication {
    public static void main(String[] args) {
        SpringApplication.run(GmallPublisherApplication.class, args);
    }

}
