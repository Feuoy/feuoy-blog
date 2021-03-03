package com.feuoy.blog.service;

import com.feuoy.blog.dao.UserRepository;
import com.feuoy.blog.po.User;
import com.feuoy.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    //根据用户名和密码验证用户登录
    @Override
    public User checkUser(String username, String password) {
        // 查询用户，密码用MD5加密后的做参数
        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));

        //return
        return user;
    }
}
