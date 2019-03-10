package com.example.cssm.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.util.Enumeration;

@Controller
public class IndexController {

    @RequestMapping(value = "/profile/{groupId}/{userId}")
    @ResponseBody
    public String profile(@PathVariable("groupId")String groupId, @PathVariable("userId")int userId,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", defaultValue = "newcode") String key){

        return String.format("GID{%s},UID{%d},TYPE{%d},KEY{%s}",groupId,userId,type,key);

    }
    @RequestMapping(value = "/vm")
    @ResponseBody
    public String news(){
        return "new";
    }

    //获取客户端信息
    @RequestMapping(value = "/request")
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session) {

        StringBuffer sb = new StringBuffer();
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            sb.append(name + " : " + request.getHeader(name) + "<br>");
        }

        for (Cookie cookie : request.getCookies()) {
            sb.append("Cookies  : ");
            sb.append(cookie.getName());
            sb.append(" : ");
            sb.append(cookie.getValue());
            sb.append("<br>");

        }

        sb.append("getMethod : " + request.getMethod() + "<br>");
        sb.append("getPathInfo :" + request.getPathInfo() + "<br>");
        sb.append("getQueryString " + request.getQueryString() + " <br>");
        sb.append("getRequestURI : " + request.getRequestURI() + " <br>");

        return sb.toString();
    }


        //设置cookie
    @RequestMapping(value = {"/response"})
    @ResponseBody
    public String response(@CookieValue(value = "nowcoderid", defaultValue = "a") String nowcoderId,
                           @RequestParam(value = "key",defaultValue = "key") String key,
                           @RequestParam(value = "value", defaultValue = "value") String value,
                           HttpServletResponse response
                           ){
        response.addCookie(new Cookie(key,value));
        response.addHeader(key,value);
        return "add  Cookie :" + key + "  -- "  + value;
    }

    //session 一般用于记录我们在网站上的行为路径
    @RequestMapping("/redirect/{code}")
    public String redirect(@PathVariable("code") int code,
                           HttpSession session){
        session.setAttribute("msg","jump from redirect");
        return "redirect:/";
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value = "key", required = false) String key) {
        if ("admin".equals(key)) {
            return "hello admin";
        }
        throw new IllegalArgumentException("Key 错误");
    }

    @RequestMapping("/c")
    public String c(){
        return "index";
    }


    @ExceptionHandler
    @ResponseBody
    public String error(Exception e){
        return "error !!!!!!" + e.getMessage();
    }


}
