package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail(){
        mailClient.sendMail("sa188@mail.ustc.edu.cn", "TEST", "Welcome");
    }


    @Test
    public void testHtmlMail(){
        Context context = new Context();
        context.setVariable("username", "sun");
//        指定模版文件路径
        //动态把数据给模版
        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);

        mailClient.sendMail("sa188@mail.ustc.edu.cn", "HTML", content);
    }
}
