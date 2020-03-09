package com.feuoy.blog.dao;

import com.feuoy.blog.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/*评论Repository*/
public interface CommentRepository extends JpaRepository<Comment,Long>{

    // 通过博客id查询List<Comment>，返回根据排序规则排序的父评论为null的评论（一级评论）
    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);

}
