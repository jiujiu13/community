package com.p1.test;

import com.p1.SpbApplication;
import com.p1.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = SpbApplication.class)
public class FilterTest {
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Test
    public void test() {
        String text="#傻#逼，你就会#吸#毒和#赌#博？#你#妈#死#了，哈哈#哈";
        String filterText = sensitiveFilter.filter(text);
        System.out.println(filterText);

    }
}
