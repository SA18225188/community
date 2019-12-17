package com.nowcoder.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/alpha")
public class AlphaController {

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "Hello Spring Boot.";
    }


    // 响应JSON对象
    //java对象 -> JSON字符串 ->JSON对象

    @RequestMapping(path = "/emp", method = RequestMethod.GET)
    //此注解可以自动转换成json字符串，要和map搭配使用,返回json格式
    @ResponseBody
    public List<Map<String, Object>> getEmp(){
        List<Map<String, Object>> emps = new ArrayList<>();
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "li");
        emp.put("age", 12);
        emps.add(emp);
        emp.put("name", "long");
        emp.put("age", 123);
        emps.add(emp);
        emp.put("name", "liong");
        emp.put("age", 1234);
        emps.add(emp);
        return  emps;
    }

}
