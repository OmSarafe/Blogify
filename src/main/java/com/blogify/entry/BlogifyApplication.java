package com.blogify.entry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.blogify.entry.enteties")
public class BlogifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogifyApplication.class, args);
    }

}
//kcsk guue kptm hqve