package com.nowcoder.community.config;

import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


//一直没发现的错误竟然是少了一个@Configuration注解
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements CommunityConstant {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //授权以下路径拥，只要拥有以下三个任意一个权限就可以访问
        //anyRequest().permitAll(),除了以上的请求，其他的都可以通过
        //初始错误：太不细心了，这样的问题不容易发现
      /*  "user/upload",
                "discuss/add",
                "comment/add/**",
                "letter/**",
                "notice/**",
                "like",
                "follow",
                "unfollow"*/
        //授权
        http.authorizeRequests()
                .antMatchers(
                        "/user/setting",
                        "/user/upload",
                        "/discuss/add",
                        "/comment/add/**",
                        "/letter/**",
                        "/notice/**",
                        "/like",
                        "/follow",
                        "/unfollow"

                )
                .hasAnyAuthority(
                        AUTHRITY_USER,
                        AUTHRITY_ADMIN,
                        AUTHRITY_MODERATOR
                )
                .antMatchers(
                        "/discuss/top",
                        "/discuss/wonderful"
                )
                .hasAnyAuthority(
                        AUTHRITY_MODERATOR
                )
                .antMatchers(
                        "/discuss/delete",
                        "/data/**",
                        "/actuator/**"

                )
                .hasAnyAuthority(
                        AUTHRITY_ADMIN
                )
                .anyRequest().permitAll()
                .and().csrf().disable();

        //权限不够的时候如何处理
        //.authenticationEntryPoint():处理未登陆时候的请求
        //.accessDeniedHandler():权限不足时候如何处理
        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {

                    //没有登陆
                    @Override
                    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        String xRequestedwith = httpServletRequest.getHeader("x-requested-with");
                        //异步请求返回json格式字符串，原来的意思为xml格式
                        if ("XMLHttpRequest".equals(xRequestedwith)) {
                            httpServletResponse.setContentType("application/plain;charset=utf-8");
                            PrintWriter printWriter = httpServletResponse.getWriter();
                            printWriter.write(CommunityUtil.getJSONString(403, "你还没有登陆"));
                        }else {
                            //否则就是返回html
                            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login");
                        }
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() {
                    //权限不足
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        String xRequestedwith = httpServletRequest.getHeader("x-requested-with");
                        //异步请求返回json格式字符串，原来的意思为xml格式
                        if ("XMLHttpRequest".equals(xRequestedwith)) {
                            httpServletResponse.setContentType("application/plain;charset=utf-8");
                            PrintWriter printWriter = httpServletResponse.getWriter();
                            printWriter.write(CommunityUtil.getJSONString(403, "你没有访问权限"));
                        }else {
                            //否则就是同步请求
                            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/denied");
                        }
                    }
                });

        //Security底层默认会拦截/logout请求，进行退出处理,filter在dispatchservlet之前执行
        //覆盖它默认的逻辑，才能执行我们自己的退出代码
        //点进去源码可以看到底层路径是logout,所以需要通过其他方式覆盖，比如程序中没有下述路径，运行到这里会自动忽略,善意的欺骗
        http.logout().logoutUrl("/securitylogout");
    }
}
