package com.nowcoder.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//标示说明是配置文件
@SpringBootApplication
public class CommunityApplication {

	//为我们自动创建spring容器
	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
	}

}
