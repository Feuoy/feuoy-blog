package com.feuoy.blog.dao;

import com.feuoy.blog.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/*
博客Repository，管理blog，
继承JpaRepository<操作对象,主键类型>，
继承JpaSpecificationExecutor，支持复杂动态查询
*/
public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    // 查询推荐top，查询后用pageable处理条数，返回List<Blog>
    //需要自定义查询时用@Query注解
    @Query("select b from Blog b where b.recommend = true")
    List<Blog> findRecommendTop(Pageable pageable);

    // 通过标题或内容查询，(String)query是查询内容，?1表示第一个参数，返回可分页的Page<Blog>
    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(String query, Pageable pageable);

    // 通过id更新阅读数，返回int修改的行数
//    dao层可不加事务注解，加了service层可自动覆盖，建议不要加
//    @Transactional
    //通知jpa，这是一个update或者delete操作，在更新或者删除操作时，此注解必须加
    @Modifying
    @Query("update Blog b set b.views = b.views+1 where b.id = ?1")
    int updateViews(Long id);

    // 查询年份排序的博客年份List<String>
    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b group by function('date_format',b.updateTime,'%Y') order by year")
    List<String> findGroupYear();

    // 通过年份查询博客List<Blog>
    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y') = ?1")
    List<Blog> findByYear(String year);

}
