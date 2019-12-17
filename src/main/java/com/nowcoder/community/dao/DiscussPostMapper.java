package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);
//    分页要知道有多少页，查询一共多少数据，方便页面展示


    //@Param注解用于给参数取别名，如果只有一个参数，并且要在<if>中使用，必须加上别名
    int selectDiscussPostRows(@Param("userId") int userId);

}
