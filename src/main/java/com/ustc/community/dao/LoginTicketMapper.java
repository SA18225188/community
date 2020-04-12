package com.ustc.community.dao;


import com.ustc.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;


//mapper表明这是数据访问对象 需要容器来管理
//Deprecated表示不推荐使用
@Mapper
@Deprecated
public interface LoginTicketMapper {

//    不仅可以用xml配置，也可以用注解实现

//    养成一个习惯，每句后面加一个空格
//    #是获取对象中的相关属性
//    利用option自动生成
    @Insert({
            "insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);


    @Select({
            "Select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket = #{ticket}"
    })
    LoginTicket selectByTicket(String ticket);


    //    互联网真正删除数据的情况很少，很多情况就是修改一下状态
    @Update({
//            可以利用scrip实现动态脚本
            //如果想用if标签，前面需要套上<script>
            "<script>",
            "update login_ticket set status=#{status} where ticket=#{ticket}",
           "<if test=\"ticket!=null\">",
            "</if>",
            "</script>"
    })
    int updateStatus(String ticket, int status);

}
