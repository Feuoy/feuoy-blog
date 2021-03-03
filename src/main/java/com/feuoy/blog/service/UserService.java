package com.feuoy.blog.service;

import com.feuoy.blog.po.User;

public interface UserService {

    //根据用户名和密码验证用户登录
    User checkUser(String username, String password);

}
