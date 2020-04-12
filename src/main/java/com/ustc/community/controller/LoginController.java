package com.ustc.community.controller;

import com.google.code.kaptcha.Producer;
import com.ustc.community.entity.User;
import com.ustc.community.service.UserService;
import com.ustc.community.util.CommunityConstant;
import com.ustc.community.util.CommunityUtil;
import com.ustc.community.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
public class LoginController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Autowired
    private RedisTemplate redisTemplate;


    @Value(value = "${server.servlet.context-path")
    private String contextPath;

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage(){
        return "/site/register";
    }

    //登陆页面
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage(){
        return "/site/login";
    }

    //生成验证码图片
    //需要使用response主动放入， 验证码较为私密，所以需要存放在session
    //验证码重构之前的代码
//    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
//    public void getKaptcha(HttpServletResponse response, HttpSession session){
//        //生成验证码
//        String kaptchaText = kaptchaProducer.createText();
//        BufferedImage kaptchaImage = kaptchaProducer.createImage(kaptchaText);
//
//        //验证码存入session
//        session.setAttribute("kaptcha", kaptchaText);
//        //把图片输出到浏览器显示
//        //回应的文本类型
//        response.setContentType("image/png");
//        try {
//            OutputStream outputStream = response.getOutputStream();
//            ImageIO.write(kaptchaImage,"png", outputStream);
//        } catch (IOException e) {
//            logger.error("响应验证码失败：" + e.getMessage());
//        }
//    }


    //验证码重构之后的代码

    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response){
        //生成验证码
        String kaptchaText = kaptchaProducer.createText();
        BufferedImage kaptchaImage = kaptchaProducer.createImage(kaptchaText);

        //存到redis
        //如果想存到redis，需要设置一个临时的验证码归属凭证，可以设置为简单的随机字符串
        String kaptchaOwner = CommunityUtil.generateUUID();
        Cookie cookie = new Cookie("kaptchaOwner", kaptchaOwner);
        //设置验证码失效时间
        cookie.setMaxAge(60);
        cookie.setPath(contextPath);
        response.addCookie(cookie);

        //把验证码存入redis
        String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
        redisTemplate.opsForValue().set(redisKey, kaptchaText,60, TimeUnit.SECONDS);



        //把图片输出到浏览器显示
        //回应的文本类型
        response.setContentType("image/png");
        try {
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(kaptchaImage,"png", outputStream);
        } catch (IOException e) {
            logger.error("响应验证码失败：" + e.getMessage());
        }
    }


    //spring调用这个register方法到时候就会把user放到model里面
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user){
        Map<String, Object> map = userService.register(user);
        if (map.isEmpty()){
            model.addAttribute("msg", "注册成功，我们已经像你的邮箱发送一封激活邮件，请尽快激活！");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        }else {

            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }

//    访问的路径格式
    // http://localhost:8080/community/activation/101/code

    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code){
        int result = userService.activation(userId, code);
        //成功返回登陆成功页面
        if (result == ACTIVATION_SUCCESS){
            model.addAttribute("msg", "激活成功,你的账号可以使用了！");
            model.addAttribute("target", "/login");

        //已经激活成功了返回主页面
        }else if (result == ACTIVATION_REPEAT){

            model.addAttribute("msg", "无效操作，你的账号已经被激活！");
            model.addAttribute("target", "/index");
            //失败返回主页面
        }else {
            model.addAttribute("msg", "激活失败，你提供的激活码不正确！");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }


    //之前代码需要session取验证码，进行比较，需要cookie保存，创建cookie需要response
    //重构之前的代码
//    @RequestMapping(path = "/login", method = RequestMethod.POST)
//    public String login(String username, String password, String code, boolean rememberme,
//                        Model model, HttpSession session, HttpServletResponse response){
//        //先判断验证码
//        String kaptcha = (String)session.getAttribute("kaptcha");
//        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)){
//            model.addAttribute("codeMsg", "验证码不正确");
//            return "/site/login";
//        }
//
//        //检查账号，密码
    //勾出remember之后会存放时间比较长
//        int expiredSeconds = rememberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPORED_SECONDS;
//        Map<String, Object> map = userService.login(username, password, expiredSeconds);
//        if (map.containsKey("ticket")){
//            //利用cookie存在本地
//            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
//            //设置cookie在哪些路径下有效， 这里需要全路径下有效
//            cookie.setPath(contextPath);
//            cookie.setMaxAge(expiredSeconds);
//            response.addCookie(cookie);
//            return "redirect:/index";
//        }else {
//
//            //错误信息传输到前端
//            model.addAttribute("usernameMsg", map.get("usernameMsg"));
//            model.addAttribute("passwordMsg", map.get("passwordMsg"));
//            return "/site/login";
//        }
//    }


    //redis重构之后的代码
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, String code, boolean rememberme,
                        Model model, /*HttpSession session,*/ HttpServletResponse response, @CookieValue("kaptchaOwner") String kaptchaOwner){
        //先判断验证码
//        String kaptcha = (String)session.getAttribute("kaptcha");
        String kaptcha = null;
        if (StringUtils.isNotBlank(kaptchaOwner)) {
            String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
            kaptcha = (String) redisTemplate.opsForValue().get(redisKey);
        }

        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg", "验证码不正确");
            return "/site/login";
        }

        //检查账号，密码
        int expiredSeconds = rememberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPORED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if (map.containsKey("ticket")){
            //利用cookie存在本地
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            //设置cookie在哪些路径下有效， 这里需要全路径下有效
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        }else {

            //错误信息传输到前端
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }
    }




    @RequestMapping(path = "/logout", method = RequestMethod.GET)
//    从cookie中取ticket
//    springmvc通过@CookieValue获取浏览器cookie
    public String Logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }
}
