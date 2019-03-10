package com.example.cssm.controller;


import com.example.cssm.util.ToutiaoUtil;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class MyErrorController implements ErrorController {

    private static final String NOT_FOUND = "404";
    private static final String ERROR_PATH = "/error";

    @RequestMapping(value = ERROR_PATH)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleError() {
        return ToutiaoUtil.getJSONString(404,"路径错误");
    }
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
