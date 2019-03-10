package com.example.cssm.async;
import com.alibaba.fastjson.JSON;
import com.example.cssm.aspect.LogAspect;

import com.example.cssm.util.JedisAdapter;
import com.example.cssm.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    JedisAdapter jedisAdapter;

    private Map<EventType, List<EventHandler>> config = new ConcurrentHashMap<EventType,List<EventHandler>>();
    private  ApplicationContext applicationContext;
    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);

        if (beans != null){
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()){ //遍历handler
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes(); //
                for (EventType type : eventTypes){
                    if (!config.containsKey(type)){
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //从队列一直消费
                while(true){
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> messages = jedisAdapter.brpop(0,key);
                    System.out.println("消费队列: messages size " + messages.size());
                    //第一个元素是队列名字
                    for(String message : messages){
                        if(message.equals(key)){
                            continue;
                        }

                        EventModel eventModel = JSON.parseObject(message,EventModel.class);
                        //找到这个事件的处理handler列表
                        System.out.println("找到这个事件的处理handler列表 : " + eventModel.getType());
                        if(!config.containsKey(eventModel.getType())){
                            logger.error("不能识别的事件");
                            continue;
                        }

                        for(EventHandler handler : config.get(eventModel.getType())){
                            System.out.println("处理事件 ： " + eventModel.getType() + " " + handler.getClass() );
                            handler.doHandler(eventModel);
                        }
                    }
                }
            }
        });

        thread.start();
    }




    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
