package com.feuoy.blog;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*声明一个NotFoundException异常类，继承运行时异常*/

// 对应NOT_FOUND的异常的状态码，springboot自动配置到404.html
@ResponseStatus(HttpStatus.NOT_FOUND)

public class NotFoundException extends RuntimeException {

    //三种构造方法
    //无参构造
    public NotFoundException() {
    }

    //错误信息构造
    public NotFoundException(String message) {
        super(message);
    }

    //错误信息、可抛出的构造
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
