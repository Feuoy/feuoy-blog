package com.feuoy.blog.web;

import com.feuoy.blog.po.Comment;
import com.feuoy.blog.po.User;
import com.feuoy.blog.service.BlogService;
import com.feuoy.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

/*前台，评论Controller*/

@Controller
public class CommentShowController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    //@Value，取配置文件中的用户头像值
    @Value("${comment.avatar}")
    private String avatar;

    //博客页获取该博的评论信息请求
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        //通过博客id获取该博客下的评论信息
        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));
        //只返回刷新的commentList片段
        return "blog :: commentList";
    }

    //提交评论请求
    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session) {
        //从comment中获取blogId
        Long blogId = comment.getBlog().getId();
        // 根据获取的blogIdSET拿到的bolg给comment
        comment.setBlog(blogService.getBlog(blogId));

        //当前登录user
        User user = (User) session.getAttribute("user");
        //如果不为空
        if (user != null) {
            //设置当前登录user的avatar，但是前台没有做需要用户登录
            comment.setAvatar(user.getAvatar());
            //管理员评论，好像没用
            comment.setAdminComment(true);

            //user为空
        } else {
            //如果user的avatar没有值，上面的缺省值
            comment.setAvatar(avatar);
        }
        //commentService.saveComment
        commentService.saveComment(comment);

        //转发到"/comments/{blogId}"请求
        return "redirect:/comments/" + blogId;
    }

}
