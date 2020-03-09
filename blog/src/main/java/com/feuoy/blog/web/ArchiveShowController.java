package com.feuoy.blog.web;

import com.feuoy.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/*前台，归档Controller*/

@Controller
public class ArchiveShowController {

    @Autowired
    private BlogService blogService;

    //归档页请求
    @GetMapping("/archives")
    public String archives(Model model) {
        // Map<String, List<Blog>>，<年份，博客列表>
        model.addAttribute("archiveMap", blogService.archiveBlog());
        // 博客数量
        model.addAttribute("blogCount", blogService.countBlog());
        return "archives";
    }

}
