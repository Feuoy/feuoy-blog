package com.feuoy.blog.web.admin;

import com.feuoy.blog.po.Tag;
import com.feuoy.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/*后台，标签管理Controller*/

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    //标签列表请求
    @GetMapping("/tags")
    public String tags(
            @PageableDefault(size = 6, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        model.addAttribute("page",tagService.listTag(pageable));
        return "admin/tags";
    }

    //标签新增页请求
    @GetMapping("/tags/input")
    public String input(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tags-input";
    }

    //标签更新页请求
    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/tags-input";
    }

    //新增标签请求
    @PostMapping("/tags")
    public String post(
            @Valid Tag tag,
            BindingResult result,
            RedirectAttributes attributes
    ) {
        //@Valid注解用于校验
        //Spring验证的错误返回  ·····>  BindingResult
        //@Valid 和 BindingResult 是一一对应的

        //找到如果有与要新增的tag同名的tag1
        Tag tag1 = tagService.getTagByName(tag.getName());
        //如果tag1存在
        if (tag1 != null) {
            //注册一个错误域
            result.rejectValue("name","nameError","不能添加重复的分类");
        }

        //如果上面的错误域存在
        if (result.hasErrors()) {
            //映射到“tags-input.html”，重新来
            return "admin/tags-input";
        }

        //如果tag1不存在，保存tag
        Tag tag2 = tagService.saveTag(tag);
        //如果tag2为null，保存失败
        if (tag2 == null ) {
            attributes.addFlashAttribute("message", "新增失败");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }

        //转发到“admin/tags”请求
        return "redirect:/admin/tags";
    }

    //更新标签请求
    @PostMapping("/tags/{id}")
    public String editPost(
            @Valid Tag tag,
            BindingResult result,
            @PathVariable Long id,
            RedirectAttributes attributes) {
        /*和新增类似*/
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            result.rejectValue("name","nameError","不能添加重复的分类");
        }
        if (result.hasErrors()) {
            return "admin/tags-input";
        }
        Tag t = tagService.updateTag(id,tag);
        if (t == null ) {
            attributes.addFlashAttribute("message", "更新失败");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/tags";
    }

    //删除标签请求
    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes) {
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";
    }

}
