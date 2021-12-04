package com.p1.test;

import com.p1.SpbApplication;
import com.p1.service.DiscussPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = SpbApplication.class)
public class CaffeineTests {
    @Autowired
    private DiscussPostService discussPostService;

    @Test
    public void testCache() {
        System.out.println(discussPostService.findDiscussPosts(0, 0, 10, 1));
        System.out.println(discussPostService.findDiscussPosts(0, 0, 10, 1));
        System.out.println(discussPostService.findDiscussPosts(0, 0, 10, 1));
        System.out.println(discussPostService.findDiscussPosts(0, 0, 10, 0));
    }
}
