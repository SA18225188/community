package com.nowcoder.community.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommunityUtil {

//    生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
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



    //服务器返回浏览器有几种
//    1. 返回一些数字，分别代表一些意思
//    2.  返回提示信息 成功还是失败
//    3. 返回业务数据
    /**
     * 把以上三种信息包装成json对象
     */

    //ajax中得到利用
    public static String getJSONString(int code, String msg, Map<String, Object> map){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        if (map != null){
            //遍历key
            for(String key: map.keySet()){
                jsonObject.put(key, map.get(key));
            }
        }
        return jsonObject.toJSONString();
    }

//    方法重栽

    public static String getJSONString(int code, String msg){
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(int code){
        return getJSONString(code, null, null);
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "zhangsan");
        map.put("age", 25);
        System.out.println(getJSONString(0, "ok", map));
    }
}
