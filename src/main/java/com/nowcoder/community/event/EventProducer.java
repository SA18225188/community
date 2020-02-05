package com.nowcoder.community.event;


import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    //处理事件
    public void fireEvent(Event event) {
        //把事件发送到指定到主题，发送的内容是Event转化的json格式
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));

    }
}
