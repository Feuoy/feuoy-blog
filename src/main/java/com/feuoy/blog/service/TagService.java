package com.feuoy.blog.service;

import com.feuoy.blog.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/*TagService*/
public interface TagService {

    //根据id获取tag
    Tag getTag(Long id);

    //根据标签名获取tag
    Tag getTagByName(String name);

    //传入tag参数新增tag
    Tag saveTag(Tag type);

    //传入id、tag更新tag
    Tag updateTag(Long id, Tag type);

    //根据id删除tag
    void deleteTag(Long id);

    // 获取所有List<Tag>
    List<Tag> listTag();

    //根据size获取一定排序规则的List<Tag>（一定排序规则在impl中加入实现）
    List<Tag> listTagTop(Integer size);

    //根据某一博客标签群的id（1,2,3的string），获取List<Tag>
    List<Tag> listTag(String ids);

    //根据Pageable获取Page<Tag>
    Page<Tag> listTag(Pageable pageable);

}
