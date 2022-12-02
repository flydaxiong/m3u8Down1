package com.xq.m3u8down;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xq.m3u8down.util.JacksonUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;


@SpringBootApplication
@EnableScheduling
public class M3u8DownApplication implements WebFluxConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(M3u8DownApplication.class, args);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/html/");
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtil.getObjectMapper();
    }

}
