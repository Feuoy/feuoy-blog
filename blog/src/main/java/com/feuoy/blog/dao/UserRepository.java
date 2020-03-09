package com.feuoy.blog.dao;

import com.feuoy.blog.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

/*用户Repository*/
public interface UserRepository extends JpaRepository<User,Long> {

    // 通过用户名和密码查询用户
    User findByUsernameAndPassword(String username, String password);

}
