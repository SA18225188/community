package com.nowcoder.community.util;


//用来生成redis的key
public class RedisKeyUtil {

    private static final String SPLIT = ":";

    //前缀
    private static final String PREFIX_ENTITY_LIKE = "like:entity";

//    以user为key
    private static final String PREFIX_USER_LIKE = "like:user";

    //关注的目标
    private static final String PREFIX_FOLLOWEE = "followee";

    private static final String PREFIX_FOLLOWER = "follower";


    //验证码
    private static final String PREFIX_KAPTCHA = "kaptcha";


    //登陆凭证
    private static final String PREFIX_TICKET = "ticket";

    //用户缓存
    private static final String PREFIX_USER = "user";


    //某个实体的赞
    //like:entity:entityType:entityId->set(userId)
    public static String getEntityLikeKey(int entityType, int entityId){
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    //某个用户的赞
    public static String getUserLikeKey(int userId){
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    //某个用户关注的实体
    //有序集合是为了比较好的统计
    //followee:userId:entityType -> zset(entityId, now)  以时间作为分数
    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    //某个实体拥有的粉丝
    //follower:entityType:entityid -> zset(userId, now)

    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    //登陆验证,验证码的owner是用户临时凭证
    public static String getKaptchaKey(String owner) {
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    //登陆的凭证
    public static String getTicketKey (String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    //用户
    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }
}
