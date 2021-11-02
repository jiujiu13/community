package com.p1.service;

import com.p1.dao.LoginTicketDao;
import com.p1.dao.UserDao;
import com.p1.pojo.LoginTicket;
import com.p1.pojo.User;
import com.p1.util.CommunityConstant;
import com.p1.util.CommunityUtil;
import com.p1.util.DateUtil;
import com.p1.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginTicketDao loginTicketDao;

    //注入邮箱客户端
    @Autowired
    private MailClient mailClient;

    //注入模板引擎
    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    public User findUserById(int id){
        return userDao.selectById(id);
    }

    //注册用户
    public void register(User user) {

        // 注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userDao.insertUser(user);

        // 激活邮件
        Context context = new Context();
        //传用户名
        context.setVariable("username",user.getUsername());
        //传邮箱
        context.setVariable("email", user.getEmail());
        //传日期，注意要改格式
        context.setVariable("creatTime", DateUtil.date2String(user.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        // http://localhost:8080/activation/101/code
        String url = domain + "/activation/" + user.getId() + "/" + user.getActivationCode();
        //传url
        context.setVariable("url", url);

        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);
    }

    //是否激活
    public int activation(int userId, String code) {
        User user = userDao.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userDao.updateStatus(userId, 1);
//            clearCache(userId);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

    //登录验证
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("msg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空!");
            return map;
        }

        // 验证账号
        User user = userDao.selectByName(username);
        if (user == null) {
            map.put("msg", "该账号不存在!");
            return map;
        }

        // 验证状态
        if (user.getStatus() == 0) {
            map.put("msg", "该账号未激活!");
            return map;
        }

        // 验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("msg", "密码不正确!");
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketDao.insertLoginTicket(loginTicket);

//        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
//        redisTemplate.opsForValue().set(redisKey, loginTicket);

        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    //注销
    public void cancel(String ticket) {
        loginTicketDao.updateStatus(ticket, 1);
//        String redisKey = RedisKeyUtil.getTicketKey(ticket);
//        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
//        loginTicket.setStatus(1);
//        redisTemplate.opsForValue().set(redisKey, loginTicket);
    }

    //查询loginTicket
    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketDao.selectByTicket(ticket);
//        String redisKey = RedisKeyUtil.getTicketKey(ticket);
//        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

    //更改图片路径
    public int updateHeader(int userId, String headerUrl) {
        return userDao.updateHeader(userId, headerUrl);
//        int rows = userDao.updateHeader(userId, headerUrl);
//        clearCache(userId);
//        return rows;
    }

}
