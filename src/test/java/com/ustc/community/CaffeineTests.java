package com.ustc.community;

import com.ustc.community.entity.DiscussPost;
import com.ustc.community.service.DiscussPostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class CaffeineTests {

    @Autowired
    private DiscussPostService postService;


    //压力测试，构造数据
    @Test
    public void initDataForTest() {
        for (int i = 0; i < 200000; i++) {
            DiscussPost post = new DiscussPost();
            post.setUserId(111);
            post.setTitle("互联网求职暖春计划");
            post.setContent("今年的就业形势，确实不容乐观。过了个年，仿佛跳水一般，整个讨论区哀鸿遍野！19届真的没人要了吗？！18届被优化真的没有出路了吗？！大家的“哀嚎”与“悲惨遭遇”牵动了每日潜伏于讨论区的牛客小哥哥小姐姐们的心，于是牛客决定：是时候为大家做点什么了！为了帮助大家度过“寒冬”，牛客网特别联合60+家企业，开启互联网求职暖春计划，面向18届&19届，拯救0 offer！");
            post.setCreateTime(new Date());
            post.setScore(Math.random() * 2000);
            postService.addDiscussPost(post);
        }
    }

    @Test
    public void testCache() {

        //三次访问显示只打印一个数据库日志就对了
        System.out.println(postService.findDiscussPostList(0, 0, 10, 1));//缓存没有
        System.out.println(postService.findDiscussPostList(0, 0, 10, 1));//这一次可以访问缓存
        System.out.println(postService.findDiscussPostList(0, 0, 10, 1));

        //按照默认方式，不走缓存，只访问数据库
        System.out.println(postService.findDiscussPostList(0, 0, 10, 0));
    }

}
