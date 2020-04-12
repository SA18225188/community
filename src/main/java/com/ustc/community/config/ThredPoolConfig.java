package com.ustc.community.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


//必须@EnableScheduling配置之后才能生效
@Configuration
@EnableScheduling
@EnableAsync
public class ThredPoolConfig {
}
