package com.feuoy.blog.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;

/*Controller异常处理Handler，通过@ControllerAdvice做Controller异常处理*/

/*Controller建议，经常用来做全局异常处理、全局数据绑定、全局数据预处理*/
@ControllerAdvice

public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 传入request和exception，返回一个ModelAndView
    //异常处理Handler，传入Exception.class
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {

        // logger记录
        logger.error("Request URL : {}，Exception : {}", request.getRequestURL(), e.getMessage());

        // 如果定义了状态码（404，500），抛出异常
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        // 如果没有定义，通过给mv添加 url和exception信息，设置当前页面为error.html，
        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception", e);
        mv.setViewName("error/error");
        return mv;
    }

}
