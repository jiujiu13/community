package com.p1.config;

import com.p1.controller.interceptor.LoginRequiredInterceptor;
import com.p1.controller.interceptor.LoginTicketInterceptor;
import com.p1.controller.interceptor.MessageRequiredInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    private MessageRequiredInterceptor messageRequiredInterceptor;
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("go");
        registry.addViewController("/index.html").setViewName("index");
        registry.addViewController("/go.html").setViewName("go");
        registry.addViewController("/charts.html").setViewName("charts");
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/tables.html").setViewName("tables");
        registry.addViewController("/forms.html").setViewName("forms");
        registry.addViewController("/site/register.html").setViewName("/site/register");
        registry.addViewController("/site/register-ok.html").setViewName("/site/register-ok");
        registry.addViewController("/site/email-operate.html").setViewName("/site/email-operate");
        registry.addViewController("/site/discuss-detail.html").setViewName("/site/discuss-detail");
        registry.addViewController("/site/letter.html").setViewName("/site/letter");
        registry.addViewController("/site/user-comment.html").setViewName("/site/user-comment");
        registry.addViewController("/error/message-error.html").setViewName("/error/message-error");
        registry.addViewController("/error/500.html").setViewName("/error/500");
        //测试jdbc能否使用
//        registry.addViewController("/addUser").setViewName("");
    }

    //配置拦截器


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登陆凭证拦截
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/css/**","/fonts/**","/img/**","/js/**","/vendor/**");

        //登录拦截（有些功能必须登录）
        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/css/**","/fonts/**","/img/**","/js/**","/vendor/**");

        //私信拦截（不能查看别人的私信）
        registry.addInterceptor(messageRequiredInterceptor)
                .excludePathPatterns("/css/**","/fonts/**","/img/**","/js/**","/vendor/**");
    }
}
