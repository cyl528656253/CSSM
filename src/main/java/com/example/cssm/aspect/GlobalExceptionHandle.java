package com.example.cssm.aspect;

import com.example.cssm.controller.CommentController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Date;

//ControllerAdvice 只能捕获Controller的异常，一般用于数据库调用出错用于事件回滚
//异常切面

//@ControllerAdvice
public class GlobalExceptionHandle {
 /*   private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandle.class);


    @ExceptionHandler(NoHandlerFoundException.class)
    public String globalExceptionHandler(Exception e){
        Date date = new Date();
        logger.error(date + " -->   " + e.getMessage() );
        return "error";

    }
    */
}
