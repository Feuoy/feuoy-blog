package com.feuoy.blog.service;

import com.feuoy.blog.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/*TypeService*/
public interface TypeService {

    /*参考TagService*/

    Type getType(Long id);

    Type getTypeByName(String name);

    Type saveType(Type type);

    Type updateType(Long id, Type type);

    void deleteType(Long id);

    List<Type> listType();

    List<Type> listTypeTop(Integer size);

    Page<Type> listType(Pageable pageable);

}
