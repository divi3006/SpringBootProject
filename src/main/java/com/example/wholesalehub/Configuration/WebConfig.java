package com.example.wholesalehub.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map all /uploads/** URLs to the uploads folder on disk
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/"); // "file:" points to filesystem
    }
}