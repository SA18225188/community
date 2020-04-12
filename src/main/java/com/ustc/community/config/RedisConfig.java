package com.ustc.community.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

    //想要把哪个对象装入容器就是返回某些值，方法名就是bean名字
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        //配置这个主要是配置序列化方式

        //设置key序列化方式
        template.setKeySerializer(RedisSerializer.string());
        //设置value序列化方式,这个序列化json比较合适，恢复比较容易
        template.setValueSerializer(RedisSerializer.json());
        //设置hash的key序列化方式
        template.setHashKeySerializer(RedisSerializer.string());
        //设置hash的value序列化方式
        template.setHashValueSerializer(RedisSerializer.json());

        //设置完触发以下生效
        template.afterPropertiesSet();
        return template;
    }
}
