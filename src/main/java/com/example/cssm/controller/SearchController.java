package com.example.cssm.controller;

import com.example.cssm.domain.HostHolder;
import com.example.cssm.domain.News;
import com.example.cssm.domain.User;
import com.example.cssm.domain.view.ViewResult;
import com.example.cssm.service.NewsService;
import com.example.cssm.service.SearchService;
import com.example.cssm.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


@Controller
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);


    @Autowired
    NewsService newsService;

    @Autowired
    SearchService searchService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;



    @RequestMapping(path = {"/search"}, method = {RequestMethod.GET})
    public String search(Model model, @RequestParam("q") String keyword,
                         @RequestParam(value = "offset", defaultValue = "0") int offset,
                         @RequestParam(value = "count", defaultValue = "20") int count) {

        try {

            List<News> newsList = searchService.searchQuestion(keyword,offset,count,"<em>", "</em>");
            logger.info("正在搜索。。。");
            List<ViewResult> viewResultList = new ArrayList<>();
            for (News news : newsList){
                News n = newsService.getById(news.getId());
                User user = userService.getUser(news.getUserId());
                ViewResult viewResult = new ViewResult();
                viewResult.setUser(user);
                viewResult.setNews(n);
                viewResult.setLike(n.getLikeCount());
                viewResultList.add(viewResult);
            }
            if (viewResultList.size() == 0)
                logger.info("搜索结果为空");
            model.addAttribute("vos",viewResultList);
            model.addAttribute("user",hostHolder.getUser());
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return "result";
    }


}
