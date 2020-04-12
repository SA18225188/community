package com.ustc.community.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

//@Component
//@Aspect
public class ServiceLogAspect {



    //描述哪些方法需要处理
//    *表示所有的返回值，com.nowcoder.community.service包名，*是所有的业务组件，*表示所有方法 ，(..)表示所有的参数
    //因为注入log，需要实例日志
    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
    public void pointcut() {

    }


    //除了环绕组件，也可以植入连接点JoinPoint
    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        // 日志格式：用户[1.2.3.4],在[xxx时间],访问了[com.nowcoder.community.service.xxx()功能].
       //以下代码目的是获取ip
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //表示这是一个特殊的日志，就不记录日志了
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getRemoteHost();
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //joinpoint.getSignature():(signature是信号,标识的意思):获取被增强的方法相关信息；
        // getDeclaringTypeName和getname();前者是一个返回方法所在的包名和类名后者是返回方法名
        String target = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        logger.info(String.format("用户[%s],在[%s],访问了[%s].", ip, now, target));
    }

}
