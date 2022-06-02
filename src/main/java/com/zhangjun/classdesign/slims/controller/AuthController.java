package com.zhangjun.classdesign.slims.controller;

import com.zhangjun.classdesign.slims.entity.User;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.service.UserService;
import com.zhangjun.classdesign.slims.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 张钧
 * @Description
 * @create 2022-06-02 21:33
 */
@RestController
@Slf4j
@RequestMapping("/slims/auth")
public class AuthController {

    @Resource
    UserService userService;

    @PostMapping
    public Result login(@RequestBody User user,
                        HttpSession session,
                        HttpServletResponse response){
        User loginUser = userService.getUser(user);
        if(loginUser != null){
            if(loginUser.getStatus() == 0){
                return Result.error("您的账户已停用");
            }else if(loginUser.getPassword().equals(user.getPassword())){
                Cookie cookie = new Cookie("loginUser",loginUser.getName());
                response.addCookie(cookie);
                session.setAttribute("loginUser",loginUser);
                Map<String,Object> map = new HashMap<>(2);
                map.put("userName",loginUser.getName());
                map.put("menus",loginUser.getMenus());
                log.info("用户登陆成功，用户：{}", loginUser);
                return Result.ok().setData(map);
            }
        }
        log.warn("用户登陆失败，用户：{}", user);
        return Result.error("登陆错误");
    }

    @GetMapping
    public Result logout(HttpServletRequest request,
                         HttpSession session){
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        if(loginUser != null){
            log.info("用户安全退出，用户：{}", MyInterceptor.threadLocal.get());
            session.invalidate();
            return Result.ok();
        }
        return Result.error(300, "请先登录");
    }
}
