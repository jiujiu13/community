package com.p1.controller;

import com.p1.dao.UserDao;
import com.p1.pojo.User;
import com.p1.util.CommunityUtil;
import com.p1.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/check")
public class CheckController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private HostHolder hostHolder;

    //验证修改密码原密码是否正确
    @RequestMapping("/oldPassword")
    @ResponseBody
    public String checkPassword(@RequestParam("oldPassword") String oldPassword) {
        User user = hostHolder.getUser();
        oldPassword = CommunityUtil.md5(oldPassword + user.getSalt());
        if (!user.getPassword().equals(oldPassword)) {
            return CommunityUtil.getJSONString(1, "原密码不正确");
        } else {
            return CommunityUtil.getJSONString(0);
        }
    }
}
