package com.ustc.community.entity;

import java.util.HashMap;
import java.util.Map;

public class Event {


    //其实就是事件类型
    private String topic;

    //事件的发起人
    private int userId;

    //需要知道这个人做了什么操作
    private int entityType;
    private int entityId;

    //这个实体的作者
    private int entityUserId;

    //可能还需要其他的额外的字段，具有扩展性
    private Map<String, Object> data = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }


    //改造一下set之后返回当前对象
    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
