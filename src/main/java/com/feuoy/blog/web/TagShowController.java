package com.feuoy.blog.web;

import com.feuoy.blog.po.Tag;
import com.feuoy.blog.service.BlogService;
import com.feuoy.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/*前台，标签Controller*/

@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    //标签页获取一些model内容
    @GetMapping("/tags/{id}")
    public String tags(
            @PageableDefault(size = 4, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable Long id,
            Model model
    ) {
        //标签数，用一个达不到的10000
        List<Tag> tags = tagService.listTagTop(10000);

        //如果没有当前选中标签，默认第一个
        if (id == -1) {
           id = tags.get(0).getId();
        }

        //和分类不同，这里是返回所有博客，考虑一下
        //所有标签、分页博客、选中的标签id
        model.addAttribute("tags", tags);
        model.addAttribute("page", blogService.listBlog(id, pageable));
        model.addAttribute("activeTagId", id);

        //映射到“tags.html”
        return "tags";
    }

}
