package com.example.cssm.controller;



import com.example.cssm.dao.NewsDAO;
import com.example.cssm.dao.UserDAO;
import com.example.cssm.domain.EntityType;
import com.example.cssm.domain.HostHolder;
import com.example.cssm.domain.News;
import com.example.cssm.domain.User;
import com.example.cssm.domain.view.ViewResult;
import com.example.cssm.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    NewsDAO newsService;

    @Autowired
    UserDAO userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    private List<ViewResult> getNews(int userId, int offset, int limit) {
        List<News> newsList = newsService.selectByUserIdAndOffset(userId, offset, limit);
        List<ViewResult> vos = new ArrayList<>();
      //  System.out.println("newsList ...: " + newsList.size());
        for (News news : newsList) {
            ViewResult vo = new ViewResult();

            vo.setNews(news);
         //   System.out.println("news.getUserId()  : "+ news.getUser_id());
            User temp = userService.selectById(news.getUserId());
            System.out.println(temp.getId());
            System.out.println(temp.getName());
            System.out.println(temp.getHeadUrl() + " user  head_url");
            vo.setUser(userService.selectById(news.getUserId()));
            vo.setLike(news.getLikeCount());

            vos.add(vo);
        }
        return vos;
    }
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model,
                        @RequestParam(value = "pop", defaultValue = "0") int pop) {

        model.addAttribute("vos",getNewsDto(3,0,30));
        model.addAttribute("user",hostHolder.getUser());
        if(hostHolder.getUser() != null){
            pop = 0;
        }
        model.addAttribute("pop",pop);
        return "home";

    }
    private List<ViewResult> getNewsDto(int userId, int offset, int limit){
        // List<News> newsList = newsService.getLatestNews(userId,offset,limit);
        List<News> newsList = newsService.selectNewsByOffset(offset,limit);
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<ViewResult> vos = new ArrayList<>();
        for(News news : newsList){
            ViewResult vo = new ViewResult();
            vo.setNews(news);
            vo.setUser(userService.selectById(news.getUserId()));
            if(localUserId != 0){
                vo.setLike(likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS,news.getId()));
            }else {
                vo.setLike(0);
            }
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getNews(userId, 0, 10));
        return "home";
    }
/*
    @RequestMapping(path = {"/error"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String error(){
        return "error";
    }
*/


}
