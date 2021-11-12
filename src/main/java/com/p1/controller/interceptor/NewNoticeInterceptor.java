package com.p1.controller.interceptor;

import com.p1.pojo.User;
import com.p1.service.MessageService;
import com.p1.util.CommunityConstant;
import com.p1.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class NewNoticeInterceptor implements HandlerInterceptor, CommunityConstant {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            //所有未读评论的数量
            int unreadComment = messageService.findNoticeUnreadCount(user.getId(), TOPIC_COMMENT);
            modelAndView.addObject("unreadComment", unreadComment);
            //所有未读赞的数量
            int unreadLike = messageService.findNoticeUnreadCount(user.getId(), TOPIC_LIKE);
            modelAndView.addObject("unreadLike", unreadLike);
            //所有未读关注的数量
            int unreadFollow = messageService.findNoticeUnreadCount(user.getId(), TOPIC_FOLLOW);
            modelAndView.addObject("unreadFollow", unreadFollow);
        }
    }
}
