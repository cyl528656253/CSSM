package com.example.cssm.controller;


import com.example.cssm.async.EventModel;
import com.example.cssm.async.EventProducer;
import com.example.cssm.async.EventType;
import com.example.cssm.domain.EntityType;
import com.example.cssm.domain.HostHolder;
import com.example.cssm.domain.News;
import com.example.cssm.service.LikeService;
import com.example.cssm.service.NewsService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LikeController {
    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private NewsService newsService;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(value = {"/like"},method = {RequestMethod.GET, RequestMethod.POST})
    //@ResponseBody
    public String like(@Param("newsId") int newsId){
        long likeCount = likeService.like(hostHolder.getUser().getId(),
                EntityType.ENTITY_NEWS,newsId);
        System.out.println("like likecount " + likeCount);
        //更新喜欢数
        News news = newsService.getById(newsId);
        newsService.updateLikeCount(newsId,(int)likeCount);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setEntityOwnerId(news.getUserId())
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(newsId));
        //return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
        return  "redirect:/";
    }

    @RequestMapping(value = {"/dislike"},method = {RequestMethod.GET, RequestMethod.POST})
    //@ResponseBody
    public String dislike(@Param("newsId") int newsId){
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS,newsId);
        //更新喜欢数
        newsService.updateLikeCount(newsId,(int)likeCount);

        //return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
        return  "redirect:/";
    }

}
