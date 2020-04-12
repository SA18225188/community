package com.ustc.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

//@Component
//@Aspect
public class AlphaAspect {


    //描述哪些方法需要处理
//    *表示所有的返回值，com.nowcoder.community.service包名，*是所有的业务组件，*表示所有方法 ，(..)表示所有的参数

    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
    public void pointcut() {

        System.out.println("pointcut");
    }



    @Before("pointcut()")
    public void before() {
        System.out.println("before");
    }

    @After("pointcut()")
    public void after() {
        System.out.println("after");
    }


    //有了返回值之后
    @AfterReturning("pointcut()")
    public void afterRetuning() {
        System.out.println("afterRetuning");
    }


    //抛出异常之后
    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        System.out.println("afterThrowing");
    }


    //环绕 不仅想在前面植入逻辑，还想在后面植入逻辑
    //下面的部分被运行在代理对象中
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        System.out.println("around before");
        //目标对象被处理的逻辑,可能有返回值
        Object obj = joinPoint.proceed();
        System.out.println("around after");
        return obj;
    }

}
