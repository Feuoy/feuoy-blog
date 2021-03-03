package com.feuoy.blog.service;

import com.feuoy.blog.NotFoundException;
import com.feuoy.blog.dao.BlogRepository;
import com.feuoy.blog.po.Blog;
import com.feuoy.blog.po.Type;
import com.feuoy.blog.util.MarkdownUtils;
import com.feuoy.blog.util.MyBeanUtils;
import com.feuoy.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service    //一个Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    //根据id获取博客，编辑博客、评论时使用
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    //根据id获取博客并转换，展示时使用
    @Transactional  //加上事务
    @Override
    public Blog getAndConvert(Long id) {
        //获取blog
        Blog blog = blogRepository.getOne(id);

        //如果为null
        if (blog == null) {
            //抛出notfound异常
            throw new NotFoundException("该博客不存在");
        }

        //如果不为null

        //new一个blog来操作，不要直接操作blogRepository中get到的blog
        Blog b = new Blog();
        //BeanUtils的copyProperties方法来复制数据
        BeanUtils.copyProperties(blog, b);

        //设置一下content的Markdown格式
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

        //更新浏览量
        blogRepository.updateViews(id);

        //返回new出来的b
        return b;
    }

    //根据获取数量获取推荐博客列表
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        //用Sort的by静态方法根据updateTime排序
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");

        //用PageRequest的of静态方法，取从0开始、size单页数量、sort排序规则的pageable
        Pageable pageable = PageRequest.of(0, size, sort);

        //返回根据pageable找到的博客列表
        return blogRepository.findRecommendTop(pageable);
    }

    //获取不同年份的博客列表
    @Override
    public Map<String, List<Blog>> archiveBlog() {
        //先找到年份排序的博客年份List<String>
        List<String> years = blogRepository.findGroupYear();
        //再根据不同年份放入Map<String, List<Blog>>
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        //可以有不同year的key1，和下面的List<Blog>的key2
        return map;
    }

    //计算博客数量
    @Override
    public Long countBlog() {
        //blogRepository继承了jpa实现的count()
        return blogRepository.count();
    }

    //根据blog新增博客
    @Transactional  //加事务，有操作的就要加事务
    @Override
    public Blog saveBlog(Blog blog) {
//        //若是添加
//        if (blog.getId() == null) {
//            //初始化一些需要此时set的成员变量
//            blog.setCreateTime(new Date());
//            blog.setUpdateTime(new Date());
//            blog.setViews(0);
//            //若是更新
//        } else {
//            //set更新时间
//            blog.setUpdateTime(new Date());
//        }

        //上面的应该是视频作者写错了

        //初始化一些需要此时set的成员变量
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViews(0);

        //blogRepository.save
        return blogRepository.save(blog);
    }

    //根据id、新blog更新博客
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        // 根据id获取blog
        Blog b = blogRepository.getOne(id);

        //if null
        if (b == null) {
            throw new NotFoundException("该博客不存在");
        }

        //copy，有blog属性名为空的不复制该属性名
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));

        //set更新时间
        b.setUpdateTime(new Date());

        //blogRepository.save
        return blogRepository.save(b);
    }

    //根据id删除博客
    @Transactional  //事务
    @Override
    public void deleteBlog(Long id) {
        //blogRepository.deleteById
        blogRepository.deleteById(id);
    }

    //根据分页获取博客列表，前台展示
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        //blogRepository.findAll
        return blogRepository.findAll(pageable);
    }

    //根据查询内容、分页获取博客列表，后台管理
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery) {
        //根据条件（Specification<Blog>对象），分页查询博客列表
        //blogRepository继承jpa的findAll方法，可加入Specification<Blog>参数，这个对象new出来重载里面的toPredicate方法
        return blogRepository.findAll(new Specification<Blog>() {

            //做对象查询
            /**
             * Creates a WHERE clause for a query of the referenced entity in form of a {@link Predicate} for the given
             * {@link Root} and {@link CriteriaQuery}.
             *
             * @param root must not be {@literal null}.
             * @param cq must not be {@literal null}.
             * @param cb must not be {@literal null}.
             * @return a {@link Predicate}, may be {@literal null}.
             */
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                //new一个List<Predicate>谓语列表，放CriteriaBuilder标准构造
                List<Predicate> predicates = new ArrayList<>();

                //blogQuery的标题有值的话
                if (!"".equals(blogQuery.getTitle()) && blogQuery.getTitle() != null) {
                    //添加CriteriaBuilder标准构造，title的
                    predicates.add(cb.like(root.<String>get("title"), "%" + blogQuery.getTitle() + "%"));
                }

                //type的
                if (blogQuery.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blogQuery.getTypeId()));
                }
                //recommend的
                if (blogQuery.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blogQuery.isRecommend()));
                }

                //predicates加入CriteriaQuery标准查询，ok
                cq.where(predicates.toArray(new Predicate[predicates.size()]));

                //end
                return null;
            }

        }, pageable);

    }

    //根据标签id、分页获取博客列表，后台管理
    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        //根据条件（Specification<Blog>对象），分页查询博客列表
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                //JOIN实现关联表查询
                //从root取tags添加关联到join
                Join join = root.join("tags");
                //CriteriaBuilder标准构造器为：当join走到与tagId相等时
                return cb.equal(join.get("id"), tagId);
            }
        }, pageable);

    }

    //根据查询内容，分页获取博客列表，后台管理
    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

}
