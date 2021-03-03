package com.feuoy.blog.service;

import com.feuoy.blog.po.Comment;

import java.util.List;

/*CommentService*/
public interface CommentService {

    //根据评论参数添加评论
    Comment saveComment(Comment comment);

    //根据博客id获取评论列表
    List<Comment> listCommentByBlogId(Long blogId);

}
