package com.feuoy.blog.web.admin;

import com.feuoy.blog.po.User;
import com.feuoy.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/*后台，登录Controller*/

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    // 登录页请求
    @GetMapping
    public String loginPage() {
        return "admin/login";
    }

    // 登录请求
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes) {
        //获取username、password参数，取HttpSession存session，取RedirectAttributes转发请求

        // 验证用户
        User user = userService.checkUser(username, password);
//        System.out.println(username);
//        System.out.println(password);

        //如果不为null
        if (user != null) {
            // 不要放密码进session
            user.setPassword(null);

            // 存session，存的是user对象
            session.setAttribute("user",user);

            //用返回，对应的是文件路径的templates/admin/index.html
            //return的"（/templates或/static路径下）"
            return "admin/index";

        } else {
            // 转发参数
            attributes.addFlashAttribute("message", "用户名或密码错误");

            // 用转发，对应的是请求的url
            return "redirect:/admin";
        }
    }

    // 登出请求，转发到登录页
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        //移除session
        session.removeAttribute("user");

        // 转发到登录页请求
        return "redirect:/admin";
    }
}
