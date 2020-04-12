package com.ustc.community.controller.interceptor;

import com.ustc.community.annotation.LoginRequired;
import com.ustc.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

//对标记对方法进行拦截
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod){
//            转型成handleMethod方便处理
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            Method method = handlerMethod.getMethod();
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
//          未登陆运行到标记到地方
            if (hostHolder.getUser() == null && loginRequired != null){
//                        重定向
                response.sendRedirect(request.getContextPath() + "/login");
                //拒绝后续请求
                return false;
            }
        }
        return true;
    }
}
