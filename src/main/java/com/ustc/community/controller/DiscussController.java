package com.ustc.community.controller;

import com.ustc.community.entity.*;
import com.ustc.community.event.EventProducer;
import com.ustc.community.service.CommentService;
import com.ustc.community.service.DiscussPostService;
import com.ustc.community.service.LikeService;
import com.ustc.community.service.UserService;
import com.ustc.community.util.CommunityConstant;
import com.ustc.community.util.CommunityUtil;
import com.ustc.community.util.HostHolder;
import com.ustc.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content){
        User user = hostHolder.getUser();
        if (user == null){
            return CommunityUtil.getJSONString(403, "你还未登陆");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPostService.addDiscussPost(discussPost);


        //发布帖子 异步提交给es服务器
        //触发发帖事件
        Event event = new Event()
                .setTopic(TOPIC_PUBLIC)
                .setUserId(user.getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(discussPost.getId());
        eventProducer.fireEvent(event);

        //计算帖子分数
        String redisKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(redisKey, discussPost.getId());


        //报错的情况后面处理
        return CommunityUtil.getJSONString(0, "发布成功");
    }


    //springmvc会从页面中获取Page
    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page){

        //帖子
        DiscussPost discussPost = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", discussPost);

        //  作者
        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute("user", user);


        //点赞数量
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeCount", likeCount);

        //点赞状态
        int likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeStatus", likeStatus);

        // 评论: 给帖子的评论
        // 回复: 给评论的评论
        // 评论列表

        //评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(discussPost.getCommentCount());
        List<Comment> commentList = commentService.findCommentsByEntity(ENTITY_TYPE_POST, discussPost.getId(), page.getOffset(), page.getLimit());

        //评论显示列表
        List<Map<String, Object>> commentVolList = new ArrayList<>();
        if (commentList != null){
            for (Comment comment : commentList){
                //评论vol
                Map<String, Object> commentVol = new HashMap<>();
                //评论
                commentVol.put("comment", comment);
                //作者
                commentVol.put("user", userService.findUserById(comment.getUserId()));


                //点赞数量
                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVol.put("likeCount", likeCount);

                //点赞状态
                likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());
                commentVol.put("likeStatus", likeStatus);

                //回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);

                //回复vol列表
                List<Map<String, Object>> replyVolList = new ArrayList<>();
                if (replyVolList != null){
                    for (Comment reply : replyList){
                        Map<String, Object> replyVol = new HashMap<>();
                        replyVol.put("reply", reply);
                        replyVol.put("user", userService.findUserById(reply.getUserId()));

                        //回复目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVol.put("target", target);


                        //点赞数量
                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
                        replyVol.put("likeCount", likeCount);

                        //点赞状态
                        likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());
                        replyVol.put("likeStatus", likeStatus);

                        replyVolList.add(replyVol);
                    }
                }

                commentVol.put("replys", replyVolList);

                //回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVol.put("replyCount", replyCount);

                commentVolList.add(commentVol);
            }

        }
        model.addAttribute("comments", commentVolList);
        return "/site/discuss-detail";
    }

    //置顶
    @RequestMapping(path = "/top", method = RequestMethod.POST)
    @ResponseBody
    public String setTop(int id) {
        discussPostService.updateType(id, 1);


        //发布帖子 异步提交给es服务器
        //触发发帖事件
        Event event = new Event()
                .setTopic(TOPIC_PUBLIC)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);

        return CommunityUtil.getJSONString(0);
    }


    //加精

    @RequestMapping(path = "/wonderful", method = RequestMethod.POST)
    @ResponseBody
    public String setWonderful(int id) {
        discussPostService.updateStatus(id, 1);


        //发布帖子 异步提交给es服务器
        //触发发帖事件
        Event event = new Event()
                .setTopic(TOPIC_PUBLIC)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);

        //计算帖子分数
        String redisKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(redisKey, id);

        return CommunityUtil.getJSONString(0);
    }


    //删除

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String setDelete(int id) {
        discussPostService.updateStatus(id, 2);


        //发布帖子 异步提交给es服务器
        //触发删除事件
        Event event = new Event()
                .setTopic(TOPIC_DELETE)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);

        return CommunityUtil.getJSONString(0);
    }

}
