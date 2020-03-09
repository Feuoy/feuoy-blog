package com.feuoy.blog.service;

import com.feuoy.blog.NotFoundException;
import com.feuoy.blog.dao.TagRepository;
import com.feuoy.blog.po.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    //根据id获取tag
    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagRepository.getOne(id);
    }

    //根据标签名获取tag
    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    //传入tag参数新增tag
    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    //传入id、tag更新tag
    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        //获取原tag
        Tag sourceTag = tagRepository.getOne(id);
        //如果null
        if (sourceTag == null) {
            throw new NotFoundException("不存在该标签");
        }
        //copy
        BeanUtils.copyProperties(tag,sourceTag);
        //save
        return tagRepository.save(sourceTag);
    }

    //根据id删除tag
    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    // 获取所有List<Tag>
    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    //根据size获取一定排序规则的List<Tag>（一定排序规则在impl中加入实现）
    @Override
    public List<Tag> listTagTop(Integer size) {
        //根据该标签下博客的数量排序
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        //PageRequest参数，zero-based page index、size、sort
        Pageable pageable = PageRequest.of(0, size, sort);
        //tagRepository.findTop
        return tagRepository.findTop(pageable);
    }

    //根据某一博客标签群的id（1,2,3的string），获取List<Tag>
    @Override
    public List<Tag> listTag(String ids) {
        return tagRepository.findAll();
        //最后看看什么用
        //return tagRepository.findAll(convertToList(ids));
    }

    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i=0; i < idarray.length;i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }

    //根据Pageable获取Page<Tag>
    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

}
