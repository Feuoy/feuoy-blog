package com.feuoy.blog.web;

import com.feuoy.blog.service.BlogService;
import com.feuoy.blog.service.TagService;
import com.feuoy.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*前台，IndexShowController*/

@Controller
public class IndexShowController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    //首页
    @GetMapping("/")
    public String index(
            @PageableDefault(size = 6, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        model.addAttribute("page",blogService.listBlog(pageable));
        model.addAttribute("types", typeService.listTypeTop(6));
        model.addAttribute("tags", tagService.listTagTop(12));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(4));
        return "index";
    }

    //搜素
    @PostMapping("/search")
    public String search(
            @PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam String query,
            Model model
    ) {
        //查询，要自己拼上“%%”
        model.addAttribute("page", blogService.listBlog("%"+query+"%", pageable));
        //查询的内容
        model.addAttribute("query", query);
        return "search";
    }

    //点击博客详情请求
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id,Model model) {
        model.addAttribute("blog", blogService.getAndConvert(id));
        return "blog";
    }

    //底部的获取最新博客请求
    @GetMapping("/footer/newblog")
    public String newblogs(Model model) {
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
        //映射到“_fragments.html”的“newblogList”片段
        return "_fragments :: newblogList";
    }

}
