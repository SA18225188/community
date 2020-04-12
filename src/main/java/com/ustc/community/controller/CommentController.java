package com.ustc.community.controller;

import com.ustc.community.entity.Comment;
import com.ustc.community.entity.DiscussPost;
import com.ustc.community.entity.Event;
import com.ustc.community.event.EventProducer;
import com.ustc.community.service.CommentService;
import com.ustc.community.service.DiscussPostService;
import com.ustc.community.util.CommunityConstant;
import com.ustc.community.util.HostHolder;
import com.ustc.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment){
        //当前用户增加评论
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);
         //触发评论事件
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("PostId", discussPostId);
//        帖子和评论的EntityId不一样
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getId());
        } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }

        //发布消息，调用之后，页面立刻处理消息响应.后续的消息发布由消息队列处理
        eventProducer.fireEvent(event);


        //评论之后帖子会变化，需要重新插入es
        //发布帖子 异步提交给es服务器
        //需要判断，只有评论给帖子才做类似es插入
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            //触发发帖事件
            event = new Event()
                    .setTopic(TOPIC_PUBLIC)
                    .setUserId(comment.getUserId())
                    .setEntityType(ENTITY_TYPE_POST)
                    .setEntityId(discussPostId);
            eventProducer.fireEvent(event);


            //计算帖子分数
            String redisKey = RedisKeyUtil.getPostScoreKey();
            redisTemplate.opsForSet().add(redisKey,discussPostId);
        }



        //重新回到详情页面
        return "redirect:/discuss/detail/" + discussPostId;
    }
}
