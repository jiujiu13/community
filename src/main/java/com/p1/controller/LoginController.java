package com.p1.controller;

import com.google.code.kaptcha.Producer;
import com.p1.dao.UserDao;
import com.p1.event.EventProducer;
import com.p1.pojo.Event;
import com.p1.pojo.User;
import com.p1.service.UserService;
import com.p1.util.CommunityConstant;
import com.p1.util.CommunityUtil;
import com.p1.util.HostHolder;
import com.p1.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
public class LoginController extends BaseController implements CommunityConstant{

    private final static Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private Producer kaptchaProducer;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserDao userDao;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private HostHolder hostHolder;

    //注册提交
    @RequestMapping("/signUp")
    public String Register(User user) {
        //后台还要验证数据，保证安全性
        User user1 = userDao.selectByName(user.getUsername());
        if (user1 != null) {
            return "/site/registerError";
        }
        User user2 = userDao.selectByEmail(user.getEmail());
        if (user2 != null) {
            return "/site/registerError";
        }
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            return "/site/registerError";
        }
        //验证完没有问题的话就向数据库传数据
        userService.register(user);

        // 触发发送邮箱事件
        Event event = new Event()
                .setTopic(TOPIC_REGISTER)
                .setUserId(user.getId());

        eventProducer.fireEvent(event);

        return "/site/register-ok";
    }

    //修改密码
    @PostMapping("/updatePassword")
    public String updatePassword(@RequestParam("new-password") String newPassword, @CookieValue("ticket") String ticket){
        User user = hostHolder.getUser();
        newPassword=CommunityUtil.md5(newPassword+user.getSalt());
        userDao.updatePassword(user.getId(), newPassword);
        userService.cancel(ticket);
        SecurityContextHolder.clearContext();
        return "/login";
    }

    //用户打开邮件链接
    // http://localhost:8080/community/activation/101/code
    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功,您的账号可使用!");
            model.addAttribute("msg2", "回到登录页");
            model.addAttribute("target", "/login.html");
        } else if (result == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "无效操作,该账号已激活!");
            model.addAttribute("msg2", "回到首页");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "激活失败,激活码不正确!");
            model.addAttribute("msg2", "回到首页");
            model.addAttribute("target", "/index");
        }
        return "/site/email-operate";
    }

    //验证码
    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response/*, HttpSession session*/) {
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

//         将验证码存入session
//         session.setAttribute("kaptcha", text);

        // 验证码的归属
        String kaptchaOwner = CommunityUtil.generateUUID();
        Cookie cookie = new Cookie("kaptchaOwner", kaptchaOwner);
        cookie.setMaxAge(60);
        response.addCookie(cookie);
        //将验证码存入Redis
        String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
        //验证码只存1分钟
        redisTemplate.opsForValue().set(redisKey, text, 60, TimeUnit.SECONDS);

        // 将图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            logger.error("响应验证码失败:" + e.getMessage());
        }
    }

    //登录验证
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, String code, boolean rememberme,
                        Model model, /*HttpSession session,*/ HttpServletResponse response,
                        @CookieValue("kaptchaOwner") String kaptchaOwner) {
        // 检查验证码
//         String kaptcha = (String) session.getAttribute("kaptcha");
        String kaptcha = null;
        if (StringUtils.isNotBlank(kaptchaOwner)) {
            String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
            kaptcha = (String) redisTemplate.opsForValue().get(redisKey);
        }

        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("msg", "验证码不正确!");
            return "/login.html";
        }

        // 检查账号,密码
        int expiredSeconds = rememberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/go.html";
        } else {
            model.addAttribute("msg", map.get("msg"));

            return "/login.html";
        }
    }

    //注销
    @GetMapping("/cancel")
    public String logout(@CookieValue("ticket") String ticket) {
        userService.cancel(ticket);
        SecurityContextHolder.clearContext();
        return "redirect:/go.html";
    }
}
