package com.p1.dao;

import com.p1.pojo.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
@Deprecated
public interface LoginTicketDao {

    //添加登陆凭据
    int insertLoginTicket(LoginTicket loginTicket);


    //查找登陆凭据
    LoginTicket selectByTicket(String ticket);

    //修改登陆凭据
    int updateStatus(String ticket, int status);
}
