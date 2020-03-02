package com.nowcoder.community.config;


import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;



    /*  Spring的Java配置方式是通过 @Configuration 和 @Bean 注解实现的：
        a、@Configuration 作用于类上，相当于一个xml配置文件
        b、@Bean 作用于方法上，相当于xml配置中的<bean>*/

@Configuration
public class KaptchaConfig {

    //方法名是作为返回对象的名字，下面放入spring容器中到bean的name是kaptchaProducer，value是Producer
    @Bean
    public Producer KaptchaProducer(){

        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width", "100");
        properties.setProperty("kaptcha.image.height", "40");
        properties.setProperty("kaptcha.textproducer.font.size", "32");
//        只能是"0,0,0"，不能是"0, 0, 0"
        properties.setProperty("kaptcha.textproducer.font.color", "0,0,0");
        //采用哪些字符串
        properties.setProperty("kaptcha.textproducer.char.String", "0123456789ABCDEFGHIGKLMNOPQRSTUVWXXYZ");
        //采用几个字符串
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        //你要采用哪一个噪声类，就是对图片进行干扰
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        //需要添加一些配置文件
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }

}
