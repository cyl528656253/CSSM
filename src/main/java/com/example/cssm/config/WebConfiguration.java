package com.example.cssm.config;


import com.example.cssm.interceptor.LoginRequiredInterceptor;
import com.example.cssm.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebConfiguration  implements WebMvcConfigurer {
    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    PassportInterceptor passportInterceptor;


    private  String[] myresource = {"/login","/static/**","/logout","/index","/cai","about","/resume"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录拦截的管理器
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/msg/*");
     // InterceptorRegistration registration = registry.addInterceptor(passportInterceptor);     //拦截的对象会进入这个类中进行判断
     // registration.addPathPatterns("/**");                    //所有路径都被拦截
     // registration.excludePathPatterns(myresource);       //添加不拦截路径

     // registration = registry.addInterceptor(loginRequiredInterceptor);     //拦截的对象会进入这个类中进行判断
     // registry.addInterceptor(passportInterceptor);
     // registration.addPathPatterns("/**");                    //所有路径都被拦截
     // registration.excludePathPatterns(myresource);       //添加不拦截路径



    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/**");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/**");
    }

}
