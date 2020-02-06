package com.nowcoder.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;


//标示说明是配置文件
@SpringBootApplication
public class CommunityApplication {


	//Es和Redis在netty上存在冲突，通过源码查看可以通过一些设置避免
	//这个是入口配置类，最先被加载
	//主要用来管理bean的初始化方法，由注解修饰的方法会在构造器被调用后执行
	@PostConstruct
	public void init() {
		//解决netty启动冲突问题
		//在Netty4Utils类中setAvailableProcessors看
		System.setProperty("es.set.netty.runtime.available.processors", "false");
	}

	//为我们自动创建spring容器
	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
	}

}
