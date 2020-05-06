package com.ustc.community.controller.interceptor;

import com.ustc.community.entity.LoginTicket;
import com.ustc.community.entity.User;
import com.ustc.community.service.UserService;
import com.ustc.community.util.CookieUtil;
import com.ustc.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;


    //调用时间：Controller方法处理之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从cookie中获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");

        if (ticket != null){
//            查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
//            检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())){
                User user = userService.findUserById(loginTicket.getUserId());
//                在本次请求中持有用户
//                会出现多个浏览器访问一个一个服务器情况 所以存在并发操作 考虑线程隔离 隔离存取对象
//                在处理请求的过程中线程都是存活的 ，请求处理完，服务器做好响应，线程才结束，threadlocal东西都在的
                hostHolder.setUser(user);
            }
        }
        return true;
    }


    //调用时间：Controller方法处理完之后，DispatcherServlet进行视图的渲染之前，也就是说在这个方法中你可以对ModelAndView进行操作
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null){
            modelAndView.addObject("loginUser", user);
        }
    }


    //DispatcherServlet进行视图的渲染之后,清理资源
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
