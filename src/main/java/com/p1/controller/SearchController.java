package com.p1.controller;

import com.p1.pojo.DiscussPost;
import com.p1.pojo.Page;
import com.p1.service.ElasticsearchService;
import com.p1.service.LikeService;
import com.p1.service.UserService;
import com.p1.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController implements CommunityConstant {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    //   /search?keyword=
    @RequestMapping(path="/search",method = RequestMethod.GET)
    public String search(String keyword, Page page, Model model) throws IOException {
        //分页信息
        page.setLimit(5);
        page.setPath("/search?keyword="+keyword);
        page.setRows((int) elasticsearchService.findCountByKeyword(keyword));//找所有带有关键字的帖子数量
        //搜索帖子
        List<DiscussPost> searchResult = elasticsearchService.searchDiscussPost(keyword, page.getOffset(), page.getLimit());

        //聚合结果
        List<Map<String,Object>> discussPosts=new ArrayList<>();
        if(searchResult!=null){
            for (DiscussPost post : searchResult) {
                Map<String,Object> map=new HashMap<>();
                //帖子
                map.put("post",post);
                //作者
                map.put("user",userService.findUserById(post.getUserId()));
                //点赞数
                map.put("likeCount",likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId()));
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        model.addAttribute("keyword",keyword);
        //返回模板
        return "/site/search";
    }
}
