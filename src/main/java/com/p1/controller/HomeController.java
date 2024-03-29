package com.p1.controller;

import com.p1.pojo.Comment;
import com.p1.pojo.DiscussPost;
import com.p1.pojo.Page;
import com.p1.pojo.User;
import com.p1.service.*;
import com.p1.util.CommunityConstant;
import com.p1.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @GetMapping("/index")
    public String getIndexPage(Model model, Page page, @RequestParam(name="orderMode",defaultValue = "0") int orderMode) {

        //不用model.addAttribute("page", page);model自动就有page
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index?orderMode="+orderMode);



        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(),page.getLimit(),orderMode);
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = userService.findUserById(post.getUserId());
                map.put("user", user);

                //查询帖子赞的数量
                long likeCount=likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId());
                map.put("likeCount",likeCount);

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        model.addAttribute("orderMode", orderMode);

        return "/index";
    }

    //错误请求
    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String getErrorPage() {
        return "/error/500";
    }

    //查看某个用户的帖子
    @RequestMapping(path="/userpost/{userId}",method = RequestMethod.GET)
    public String findSomeonePost(@PathVariable("userId") int userId,Model model,Page page){
        //分页
        page.setRows(discussPostService.findDiscussPostRows(userId));
        page.setLimit(5);
        page.setPath("/userpost/"+userId);

        //根据id查是谁的帖子列表
        User u = userService.findUserById(userId);
        model.addAttribute("u",u);

        List<DiscussPost> list = discussPostService.findDiscussPosts(userId, page.getOffset(),page.getLimit(),0);
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = userService.findUserById(userId);
                map.put("user", user);

                //查询帖子赞的数量
                long likeCount=likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId());
                map.put("likeCount",likeCount);

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);

        return "/site/user-post";
    }

    //查看某个用户的回复列表
    @RequestMapping(path = "/usercomment/{userId}",method = RequestMethod.GET)
    public String findSomeoneComment(@PathVariable("userId") int userId,Model model,Page page){
        //分页
        page.setRows(discussPostService.findDiscussPostRows(userId));
        page.setLimit(5);
        page.setPath("/usercomment/"+userId);

        //根据id查是谁的回复列表
        User u = userService.findUserById(userId);
        model.addAttribute("u",u);

        //查到该用户的回复列表信息
        List<Comment> list = commentService.findCommentByUserId(userId,page.getOffset(),page.getLimit());

        List<Map<String, Object>> comments = new ArrayList<>();
        if (list != null) {
            for (Comment comment : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("comment", comment);
                User user = userService.findUserById(userId);
                map.put("user", user);
                DiscussPost post=null;
                if(comment.getEntityType()==1){
                    post = discussPostService.findDiscussPostById(comment.getEntityId());
                }else if(comment.getEntityType()==2){
                    post = discussPostService.findDiscussPostById(commentService.findCommentById(comment.getEntityId()).getEntityId());
                }
                map.put("post", post);

                //查询回复收到回复的数量
                int commentCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                map.put("commentCount",commentCount);


                //查询回复收到赞的数量
                long likeCount=likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,comment.getId());
                map.put("likeCount",likeCount);

                comments.add(map);
            }
        }
        model.addAttribute("comments", comments);

        return "/site/user-comment";
    }

    @GetMapping("/deneid")
    public String getDeniedpage(){
        return "/error/404";
    }
    @GetMapping("/pleaselogin")
    public String getLoginpage(){
        return "/error/please-login";
    }

    //用拦截器实现此功能了
//    @RequestMapping(path = "/notice",method = RequestMethod.GET)
//    @ResponseBody
//    public String getNotice(Model model){
//        if(hostHolder.getUser()!=null){
//            User u = hostHolder.getUser();
//            HashMap<String,Object> map=new HashMap<>();
//            //所有未读评论的数量
//            int unreadComment = messageService.findNoticeUnreadCount(u.getId(), TOPIC_COMMENT);
//            map.put("unreadComment", unreadComment);
//            //所有未读赞的数量
//            int unreadLike = messageService.findNoticeUnreadCount(u.getId(), TOPIC_LIKE);
//            map.put("unreadLike", unreadLike);
//            //所有未读关注的数量
//            int unreadFollow = messageService.findNoticeUnreadCount(u.getId(), TOPIC_FOLLOW);
//            map.put("unreadFollow", unreadFollow);
//            return  CommunityUtil.getJSONString(0, null, map);
//        }else {
//            return CommunityUtil.getJSONString(1);
//        }
//
//    }

}

