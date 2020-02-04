package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    //点赞
    public void like(int userId, int entityType, int entityId, int entityUserId){
//        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
//        //判断是否存在，如果存在赞就取消，不存在就点赞
//        Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
//        if (isMember){
//            redisTemplate.opsForSet().remove(entityLikeKey, userId);
//        }else {
//            redisTemplate.opsForSet().add(entityLikeKey, userId);
//        }
        //一个业务包括两个操作，这里使用事务操作
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                //实体的赞
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                //统计用户被赞的数量
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);

                //在事务执行之前做判断
                Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
                redisOperations.multi();
                if (isMember) {
                    redisOperations.opsForSet().remove(entityLikeKey, userId);
                    redisOperations.opsForValue().decrement(userLikeKey);
                }else {
                    redisOperations.opsForSet().add(entityLikeKey, userId);
                    redisOperations.opsForValue().increment(userLikeKey);
                }
                return redisOperations.exec();

            }
        });
    }

    //查询某实体点赞的数量
    public long findEntityLikeCount(int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    //查询某人对某实体对点赞状态
    //这里用对int不用boolean，考虑以后可能不仅仅点赞，有可能喝彩，如果boolean就体现不出来。所以一些细节处都需要从业务层次考虑，需要考虑后期的改动
    public int findEntityLikeStatus(int userId, int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
//        如果点过的返回1，没点过返回0， 以后如果有喝彩，可以返回-1
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }

    //查询某个用户获得的赞
    public int findUserLikeCount(int userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count.intValue();
    }
}
