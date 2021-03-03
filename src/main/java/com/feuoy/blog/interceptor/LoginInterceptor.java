package com.feuoy.blog.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*登录拦截器，继承HandlerInterceptorAdapter*/

public class LoginInterceptor extends HandlerInterceptorAdapter {

    // 调用执行之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 如果session里面user为null，拦截
        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect("/admin");
            return false;
        }

        // 否则，不拦截
        return true;
    }

}
