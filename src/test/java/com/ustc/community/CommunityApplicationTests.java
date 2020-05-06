package com.ustc.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
//以下面定义的类为配置类
@ContextConfiguration(classes = CommunityApplication.class)
//实现接口得到spring容器运行之后自动得到applicationcontext
public class CommunityApplicationTests implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Test
	public void testApplicationContext(){
		System.out.println(applicationContext);
		//按照类型获取bean
		AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class);
		System.out.println(alphaDao.select());

		AlphaDao alphaDao1 = applicationContext.getBean("hiber", AlphaDao.class);
		System.out.println(alphaDao1.select());
	}

	@Test
	public void testBeanConfig(){
		SimpleDateFormat simpleDateFormat = applicationContext.getBean(SimpleDateFormat.class);

		System.out.println(simpleDateFormat.format(new Date()));
	}


	@Autowired
//	@Qualifier("hiber")
	private AlphaDao alphaDao;

	@Autowired
	private  SimpleDateFormat simpleDateFormat;

	@Test
	public void testDI(){
		System.out.println(alphaDao);
		System.out.println(simpleDateFormat.format(new Date()));

	}
}
