package com.ustc.community.controller.interceptor;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//需要交给spring容器处理
@Component
public class AlphaInterceptor implements HandlerInterceptor {

    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(AlphaInterceptor.class);
//    Controller之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("pre:" + handler.toString());
        return true;
    }

    //    Controller之后执行 模版引擎之前调用
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.debug("post:" + handler.toString());
    }

    //    TemplateEngine之后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        logger.debug("aterCompletion:" + handler.toString());
    }
}
