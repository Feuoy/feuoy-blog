package com.feuoy.blog.web.admin;

import com.feuoy.blog.po.Blog;
import com.feuoy.blog.po.User;
import com.feuoy.blog.service.BlogService;
import com.feuoy.blog.service.TagService;
import com.feuoy.blog.service.TypeService;
import com.feuoy.blog.vo.BlogQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/*后台，博客管理Controller*/

@Controller //一个Controller
@RequestMapping("/admin")   //所有请求以/admin开头
public class BlogController {

    //常用地址
    private static final String INPUT = "admin/blogs-input";    //映射“博客新增”.html
    private static final String LIST = "admin/blogs";   //映射”博客列表“.html
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";    //转发“博客列表”请求

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    //访问博客列表请求
    @GetMapping("/blogs")
    public String blogs(
            @PageableDefault(size = 4, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
            BlogQuery blogQuery,
            Model model
    ) {
        //@PageableDefault注解初始化Pageable默认值，分页长度4，排序规则updateTime，倒序
        //vo对象BlogQuery，对应视图标题、分类、推荐
        //model，for adding attributes to the model

        //add分页博客列表
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        //add分类列表
        model.addAttribute("types", typeService.listType());

        //映射到blogs.html
        return LIST;
    }

    //条件搜索博客列表请求
    @PostMapping("/blogs/search")
    public String search(
            @PageableDefault(size = 4, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
            BlogQuery blogQuery,
            Model model
    ) {
        //搜索结果添加到model
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));

        //只更新blogs.html的blogList片段
        return "admin/blogs :: blogList";
    }

    //根据id删除博客请求
    @GetMapping("/blogs/{id}/delete")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes attributes
    ) {
        //@PathVariable参数获取get请求uri中的参数，博客id
        //RedirectAttributes对象，转发请求提示消息

        //blogService.deleteBlog
        blogService.deleteBlog(id);

        //attr.addAttribute(“param”, value);
        //这种方式就相当于重定向之后，在url后面拼接参数，这样在重定向之后的页面或者控制器再去获取url后面的参数就可以了，但这个方式因为是在url后面添加参数的方式，所以暴露了参数，有风险
        //attr.addFlashAttribute(“param”, value);
        //这种方式也能达到重新向带参，而且能隐藏参数，其原理就是放到session中，session在跳到页面后马上移除对象。

        //addFlashAttribute，这里应该也可以用addAttribute
        attributes.addFlashAttribute("message", "删除成功");

        //删除后转发到“博客列表”请求
        return REDIRECT_LIST;
    }

    //博客新增页面请求
    @GetMapping("/blogs/input")
    public String input(Model model) {
        //获取types和tags给前端显示
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());

        //将要新增的Blog对象，new一下传过去填入参数
        model.addAttribute("blog", new Blog());

        //映射到“博客新增.html”
        return INPUT;
    }

    //博客编辑页面请求
    @GetMapping("/blogs/{id}/input")
    public String editInput(
            @PathVariable Long id,
            Model model
    ) {
        //编辑的博客id
        //model，放入一些对象给前端用

        //types和tags
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());

        //根据id获取博客
        Blog blog = blogService.getBlog(id);
        //初始化给tagIds成员变量，但是why在这里
        blog.init();

        //博客内容复现
        model.addAttribute("blog", blog);

        //映射到“博客新增.html”
        return INPUT;
    }

    //更新标签不行，要改一下
    // 新增或更新博客请求
    // @PostMapping("/addOrUpdateBlog")，改一下名字好，有点歧义
    @PostMapping("/blogs")
    public String post(
            Blog blog,
            RedirectAttributes attributes,
            HttpSession session
    ) {
        //新增或删除的博客对象
        //转发属性Model，可放入提示消息
        //HttpSession对象

        //从session中获取当前登录的user
        blog.setUser((User) session.getAttribute("user"));

        //blogGET到type，typeGET到id
        //从typeService中根据拿到的id用getType方法，拿到type
        //blogSET拿到的type
        blog.setType(typeService.getType(blog.getType().getId()));
        //类似
        blog.setTags(tagService.listTag(blog.getTagIds()));

        //如果传入的blog的id不在数据库中有，保存
        Blog b;
        if (blog.getId() == null) {
            b =  blogService.saveBlog(blog);

            //有的话，更新
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }

        //上面的保存或更新的博客，成功的话会返回给b，如果b为null
        if (b == null ) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }

        //完成后转发到“博客列表.html”
        return REDIRECT_LIST;
    }

}
