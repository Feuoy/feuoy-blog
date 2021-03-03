package com.feuoy.blog.dao;

import com.feuoy.blog.po.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/*类型Repository*/
public interface TypeRepository extends JpaRepository<Type,Long> {

    // 通过类型名返回Tag
    Type findByName(String name);

    // 查询类型top，查询后用pageable处理条数，返回List<Type>
    @Query("select t from Type t")
    List<Type> findTop(Pageable pageable);

}
