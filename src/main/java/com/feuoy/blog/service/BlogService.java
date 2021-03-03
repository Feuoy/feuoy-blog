package com.feuoy.blog.service;

import com.feuoy.blog.po.Blog;
import com.feuoy.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/*BlogService*/
public interface BlogService {

    //根据id获取博客，编辑博客、评论时使用
    Blog getBlog(Long id);

    //根据id获取博客并转换，展示时使用
    Blog getAndConvert(Long id);

    //根据获取数量获取推荐博客列表
    List<Blog> listRecommendBlogTop(Integer size);

    //获取不同年份的博客列表
    Map<String,List<Blog>> archiveBlog();

    //计算博客数量
    Long countBlog();

    //根据blog新增博客
    Blog saveBlog(Blog blog);

    //根据id、新blog更新博客
    Blog updateBlog(Long id, Blog blog);

    //根据id删除博客
    void deleteBlog(Long id);

    //根据分页获取博客列表，前台展示
    Page<Blog> listBlog(Pageable pageable);

    //根据查询内容、分页获取博客列表，后台管理
    Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery);

    //根据标签id、分页获取博客列表，后台管理
    Page<Blog> listBlog(Long tagId, Pageable pageable);

    //根据查询内容，分页获取博客列表，后台管理
    Page<Blog> listBlog(String query, Pageable pageable);

}
