package com.p1.dao;

import com.p1.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentDao {

    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    //查回复数量
    int selectCountByEntity(int entityType, int entityId);

    int insertComment(Comment comment);

    Comment selectCommentById(int id);

    //查某个用户的回复列表
    List<Comment> selectCommentsByUserId(int userId,int offset,int limit);

}
