package com.p1.controller;

import com.p1.dao.UserDao;
import com.p1.pojo.User;
import com.p1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//注册页异步请求
@Controller
@RequestMapping("/register")
public class RegisterController extends BaseController{
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;

    @GetMapping("/username")
    public void findUsername(@RequestParam("username") String username, Model model, HttpServletResponse response) throws IOException {
        User user = userDao.selectByName(username);

        writeValue(user,response);
    }

    @GetMapping("/email")
    public void findEmail(@RequestParam("email") String email, Model model, HttpServletResponse response) throws IOException {
        User user = userDao.selectByEmail(email);
        writeValue(user,response);
    }
}
