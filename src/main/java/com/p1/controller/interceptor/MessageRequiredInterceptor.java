package com.p1.controller.interceptor;

import com.p1.annotation.LoginRequired;
import com.p1.annotation.MessageRequired;
import com.p1.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

//拦截查看其他用户的对话
@Component
public class MessageRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = String.valueOf(request.getRequestURL());
        int number = url.lastIndexOf("/");
        String sub_url = url.substring(number);
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            MessageRequired messageRequired = method.getAnnotation(MessageRequired.class);
            if (messageRequired != null &&  !sub_url.contains(Integer.toString(hostHolder.getUser().getId()))) {
                response.sendRedirect("/error/message-error.html");
                return false;
            }
        }

        return true;
    }
}
