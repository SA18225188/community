package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommunityUtil {

//    生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll(" ", "");
    }


//    生成md5算法,只能加密 很难解密,所以需要加点盐
//    hello->dhfakhfb41ngj
//    hello + fhajnja->fhaihfifrif
    public static String md5(String key){
        if (StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
