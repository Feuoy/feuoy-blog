package com.feuoy.blog.service;

import com.feuoy.blog.dao.CommentRepository;
import com.feuoy.blog.po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    //根据评论参数添加评论
    @Transactional    //事务
    @Override
    public Comment saveComment(Comment comment) {
        //获取该评论的成员变量父评论id
        Long parentCommentId = comment.getParentComment().getId();

        //如果该评论的父评论id成员变量不为前端js设置的-1
        if (parentCommentId != -1) {
            //根据这父评论id设置该评论的父评论对象成员变量
            comment.setParentComment(commentRepository.getOne(parentCommentId));
        } else {
            //否则评论的父评论对象成员变量为null
            comment.setParentComment(null);
        }

        //该评论创建日期
        comment.setCreateTime(new Date());

        //save
        return commentRepository.save(comment);
    }

    //根据博客id获取评论列表
    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        //根据评论时间排序
        Sort sort = Sort.by("createTime");
        //返回一级评论评论列表
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);
        //遍历每个一级评论，返回List<Comment>【合并各一级评论的各层子代，赋值到各一级评论的childrenComments（List<Comment>）成员变量中】
        return eachComment(comments);
    }

    /**
     * 遍历每个一级评论，返回List<Comment>【合并各一级评论的各层子代，赋值到各一级评论的childrenComments（List<Comment>）成员变量中】
     * @param comments
     * @return List<Comment>
     */
    private List<Comment> eachComment(List<Comment> comments) {
        //结果commentsView
        List<Comment> commentsView = new ArrayList<>();

        //遍历一级评论，加到commentsView
        for (Comment comment : comments) {
            //copy
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            //add
            commentsView.add(c);
        }

        //合并各一级评论的各层子代，赋值到各一级评论的childrenComments（List<Comment>）成员变量中
        combineChildren(commentsView);

        //commentsView
        return commentsView;
    }

    //存放某一二级及以上评论的集合
    private List<Comment> tempChildren = new ArrayList<>();

    /**
     *
     * @param comments root根节点，blog不为空的对象集合
     * @return
     */
    private void combineChildren(List<Comment> comments) {
        //遍历传入的一级评论list
        for (Comment comment : comments) {

            //获取该一级评论的二级评论
            List<Comment> children = comment.getChildrenComments();

            //遍历二级评论list
            for(Comment child : children) {
                //循环迭代，找出子代，存放在tempChildrens中
                recursively(child);
            }

            //修改一级评论的Children集合，为迭代处理后的集合
            comment.setChildrenComments(tempChildren);

            //清除临时存放区
            tempChildren = new ArrayList<>();
        }
    }

    /**
     * 递归迭代，找出所有二级及以上评论的集合
     * @param comment 被迭代的对象，第一个二级评论
     * @return
     */
    private void recursively(Comment comment) {
        //comment添加到tempChildren作为顶节点
        tempChildren.add(comment);

        //如果这个comment的子一层评论数大于0
        if (comment.getChildrenComments().size()>0) {

            //获取所有子一层评论
            List<Comment> children = comment.getChildrenComments();

            //把它们一个一个放到tempChildren
            //效果是【二级评论1、[三级评论1、四级评论1.1、四级评论1.2（二级评论1的所有子评论）]、二级评论2。。。。】
            for (Comment child : children) {
                tempChildren.add(child);

                //如果评论下有还有子一层评论，递归进行
                if (child.getChildrenComments().size()>0) {
                    recursively(child);
                }
            }
        }
    }

}
