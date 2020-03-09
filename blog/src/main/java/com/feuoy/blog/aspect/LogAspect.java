package com.feuoy.blog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/*通过aop切面做日志记录*/

//aop切面
@Aspect

//组件
@Component

public class LogAspect {

    //日志记录器，从LoggerFactory中getLogger传参this.getClass()，一般声明为私有final
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 切面，所有经过com.feuoy.blog.web的所有类的所有方法
    @Pointcut("execution(* com.feuoy.blog.web.*.*(..))")

    // 记录
    public void log() {
    }

    // 在执行log()前，获取requestLog信息
    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        // JoinPoint封装了SpringAop中切面方法的信息

        // RequestContextHolder获取attributes的request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //request中获取url，ip
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();

        //目标方法，参数
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        //构造requestLog
        RequestLog requestLog = new RequestLog(url, ip, classMethod, args);

        //放进logger
        logger.info("Request : {}", requestLog);
    }

    // 执行返回之后，获取result
    @AfterReturning(returning = "result", pointcut = "log()")
    public void doAfterRuturn(Object result) {
        logger.info("Result : {}", result);
    }

    // 做一个RequestLog的内部类
    private class RequestLog {

        //成员变量、全参构造方法、toString方法，就行
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }

}
