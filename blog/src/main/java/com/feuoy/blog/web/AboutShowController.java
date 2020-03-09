package com.feuoy.blog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*前台，关于我Controller*/

@Controller
public class AboutShowController {

    // 关于我页请求
    @GetMapping("/about")
    public String about() {
        return "about";
    }

}
