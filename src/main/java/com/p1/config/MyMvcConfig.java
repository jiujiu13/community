package com.p1.config;

import com.p1.controller.interceptor.LoginRequiredInterceptor;
import com.p1.controller.interceptor.LoginTicketInterceptor;
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
        //测试jdbc能否使用
//        registry.addViewController("/addUser").setViewName("");
    }

    //配置拦截器


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/css/**","/fonts/**","/img/**","/js/**","/vendor/**");

        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/css/**","/fonts/**","/img/**","/js/**","/vendor/**");
    }
}
