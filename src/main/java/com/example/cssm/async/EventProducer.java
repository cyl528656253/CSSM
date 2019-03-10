package com.example.cssm.async;

import com.alibaba.fastjson.JSONObject;
import com.example.cssm.util.JedisAdapter;
import com.example.cssm.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel){
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key,json);
            System.out.println("产生一个异步事件：" + eventModel.getType());
            return true;
        }catch (Exception e){
            return  false;
        }
    }
}
