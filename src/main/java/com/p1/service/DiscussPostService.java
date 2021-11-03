package com.p1.service;

import com.p1.dao.DiscussPostDao;
import com.p1.pojo.DiscussPost;
import com.p1.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostDao discussPostDao;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    //查询所有帖子
    public List<DiscussPost> findDiscussPosts(int userId,int offset,int limit){
        return discussPostDao.selectDiscussPosts(userId,offset,limit);
    }

    //查询帖子总数
    public int findDiscussPostRows(int userId){
        return discussPostDao.selectDiscussPostRows(userId);
    }

    //增加帖子
    public int addDiscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 转义HTML标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        // 过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discussPostDao.insertDiscussPost(post);
    }

    //帖子详情
    public DiscussPost findDiscussPostById(int id) {
        return discussPostDao.selectDiscussPostById(id);
    }



    //增加帖子评论数
    public int updateCommentCount(int id, int commentCount) {
        return discussPostDao.updateCommentCount(id, commentCount);
    }

    public int updateType(int id, int type) {
        return discussPostDao.updateType(id, type);
    }

    public int updateStatus(int id, int status) {
        return discussPostDao.updateStatus(id, status);
    }

    public int updateScore(int id, double score) {
        return discussPostDao.updateScore(id, score);
    }


}
