package com.ustc.community.controller.advice;


import com.ustc.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


//扫描带有controller注解的bean
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {
    //一个异常处理就是要记录日志。需要把日志组件实例化
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    //Exception是所有异常的父类，所有异常都可以用这个处理
    //表示处理哪些异常
    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException{
        logger.error("服务器发生异常:" + e.getMessage());
        //遍历堆栈信息，全部显示
        for (StackTraceElement element : e.getStackTrace()){
            logger.error(element.toString());
        }
        //通过请求消息头判断是同步消息还是异步消息
        String xRequestedWith = request.getHeader("x-requested-with");
        if ("XMLHttpRequest".equals(xRequestedWith)){
            //这个请求是xml形式返回，只有异步请求才是返回xml。普通请求是要求你返回html
            //异步请求返回json格式
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter write = null;
            write = response.getWriter();
            write.write(CommunityUtil.getJSONString(1, "服务器异常"));
        }else {
            //否则就是普通的错误请求
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
}
