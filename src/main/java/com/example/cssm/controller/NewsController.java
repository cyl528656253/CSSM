package com.example.cssm.controller;
import com.example.cssm.domain.*;
import com.example.cssm.domain.view.CommentView;
import com.example.cssm.service.CommentService;
import com.example.cssm.service.NewsService;
import com.example.cssm.service.UserService;
import com.example.cssm.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @Autowired
    NewsService newsService;


    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @RequestMapping(value = {"/news/{newsId}"} ,method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model){
        try{
            News news = newsService.getById(newsId);

            if(news != null){
                int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0 ;
                if(localUserId != 0){
              //      model.addAttribute("like",likeService.getLikeStatus(localUserId,EntityType.ENTITY_NEWS,news
            //                .getId()));
                }else {
                    model.addAttribute("like",0);
                }
                List<Comment> comments = commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS);
                List<CommentView> commentVOs = new ArrayList<>();
                for(Comment comment : comments){
                    CommentView commentView = new CommentView();
                    commentView.setComment(comment);
                    commentView.setUser(userService.getUser(comment.getUserId()));
                    commentVOs.add(commentView);
                }
                model.addAttribute("comments",commentVOs);
            }

            model.addAttribute("news",news);
            model.addAttribute("owner",userService.getUser(news.getUserId()));
        }catch (Exception e){
            logger.error("获取资讯明细错误"+e.getMessage());
        }

        return "detail";
    }

    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new
                    File(ToutiaoUtil.IMAGE_DIR + imageName)), response.getOutputStream());
        } catch (Exception e) {
            logger.error("读取图片错误" + imageName + e.getMessage());
        }
    }

    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = newsService.saveImage(file);
            //String fileUrl = qiniuService.saveImage(file);
            if (fileUrl == null) {
                return ToutiaoUtil.getJSONString(1, "上传图片失败");
            }
            return ToutiaoUtil.getJSONString(0, fileUrl);
        } catch (Exception e) {
            logger.error("上传图片失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "上传失败");
        }
    }


    @RequestMapping(value = "/user/addNews/",method = {RequestMethod.GET})
    public String addNews(){
        return "addNews";
    }

    @RequestMapping(value = "/user/addNews/",method = {RequestMethod.POST})
    // @ResponseBody
    public String addNewsPost(@RequestParam("image")MultipartFile image,
                              @RequestParam("title") String title,
                              @RequestParam("link") String link){
        try{
            String fileUrl = uploadImage(image);
            if(fileUrl == null){
                return ToutiaoUtil.getJSONString(1,"上传图片失败");
            }

            News news = new News();
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setImage(fileUrl);
            news.setLink(link);
            if(hostHolder.getUser() != null){
                news.setUserId(hostHolder.getUser().getId());
            }else{
                //设置一个匿名用户
                news.setUserId(3);
            }
            newsService.addNews(news);

            // return ToutiaoUtil.getJSONString(0,fileUrl);
            return  "redirect:/";
        }catch (Exception e){
            logger.error("添加资讯失败" + e.getMessage());
            return  ToutiaoUtil.getJSONString(1,"发布资讯失败");
        }
    }
}
