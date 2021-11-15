package com.p1.test;

import com.p1.SpbApplication;
import com.p1.dao.CommentDao;
import com.p1.dao.DiscussPostDao;
import com.p1.dao.LoginTicketDao;
import com.p1.dao.UserDao;
import com.p1.pojo.Comment;
import com.p1.pojo.DiscussPost;
import com.p1.pojo.LoginTicket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;


@SpringBootTest
@ContextConfiguration(classes = SpbApplication.class)
public class TestDao {
    @Autowired
    private UserDao userDao;

    @Autowired
    private DiscussPostDao discussPostDao;

    @Autowired
    private LoginTicketDao loginTicketDao;

    @Autowired
    private CommentDao commentDao;

    @Test
    public void test(){
//        User user = userDao.selectById(101);
//        System.out.println(user);
        DiscussPost post = discussPostDao.selectDiscussPostById(51);
        System.out.println(post.getCommentCount());
    }

    @Test
    public void test2(){
        List<DiscussPost> discussPosts = discussPostDao.selectDiscussPosts(101, 0, 10);
        for (DiscussPost discussPost : discussPosts) {
            System.out.println(discussPost);
        }
    }
    @Test
    public void test3(){
        int i = discussPostDao.selectDiscussPostRows(0);
        System.out.println(i);

    }
    @Test
    public void test4(){
        LoginTicket loginTicket=new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+10*60));
        loginTicketDao.insertLoginTicket(loginTicket);
    }

    @Test
    public void testSelectLoginTicket() {
        LoginTicket loginTicket = loginTicketDao.selectByTicket("abc");
        System.out.println(loginTicket);

        loginTicketDao.updateStatus("abc", 1);
        loginTicket = loginTicketDao.selectByTicket("abc");
        System.out.println(loginTicket);
    }

    @Test
    public void test5(){
        List<Comment> comments = commentDao.selectCommentsByUserId(150, 0, 10);
        for (Comment comment : comments) {
            System.out.println(comment);
        }
    }
}
