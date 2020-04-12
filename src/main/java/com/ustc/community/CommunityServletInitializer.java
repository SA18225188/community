package com.ustc.community;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class CommunityServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        //tomcat会访问这个方法，把这个方法当作入口来运行我们的项目，声明我们的核心配置文件是CommunityApplication，用来加载配置
        return builder.sources(CommunityApplication.class);
    }
}
