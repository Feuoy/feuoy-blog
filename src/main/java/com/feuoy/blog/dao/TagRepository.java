package com.feuoy.blog.dao;

import com.feuoy.blog.po.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/*标签Repository*/
public interface TagRepository extends JpaRepository<Tag,Long> {

    // 通过标签名返回Tag
    Tag findByName(String name);

    // 查询标签top，查询后用pageable处理条数，返回List<Tag>
    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);

}
