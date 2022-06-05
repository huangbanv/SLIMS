package com.zhangjun.classdesign.slims.controller;

import com.zhangjun.classdesign.slims.entity.User;
import com.zhangjun.classdesign.slims.service.UserService;
import com.zhangjun.classdesign.slims.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
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
                        HttpSession session) {
        User loginUser = userService.getUser(user);
        if (loginUser != null) {
            ServletContext application = session.getServletContext();
            Map<String, String> loginMap = (Map<String, String>) application.getAttribute("loginMap");
            if (loginMap == null) {
                loginMap = new HashMap<>();
            } else {
                for (String key : loginMap.keySet()) {
                    if (user.getAccount().equals(key)) {
                        if (session.getId().equals(loginMap.get(key))) {
                            System.out.println("在同一地点多次登录！");
                        } else {
                            return Result.error("您的账号已在异地登陆");
                        }
                    }
                }
            }
            if (loginUser.getStatus() == 0) {
                return Result.error("您的账户已停用");
            } else if (loginUser.getPassword().equals(user.getPassword())) {
                loginMap.put(user.getAccount(), session.getId());
                application.setAttribute("loginMap", loginMap);
                session.setAttribute("loginUser", loginUser);
                Map<String, Object> map = new HashMap<>(2);
                map.put("userName", loginUser.getName());
                map.put("menus", loginUser.getMenus());
                log.info("用户登陆成功，用户：{}", loginUser);
                return Result.ok("登陆成功").setData(map);
            }
        }
        log.warn("用户登陆失败，用户：{}", user);
        return Result.error("登陆错误");
    }

    @GetMapping
    public Result logout(HttpSession session) {
        User loginUser = (User)session.getAttribute("loginUser");
        if(loginUser != null){
            ServletContext application = session.getServletContext();
            Map<String, String> loginMap = (Map<String, String>)application.getAttribute("loginMap");
            loginMap.remove(loginUser.getAccount());
            application.setAttribute("loginMap",loginMap);
            session.invalidate();
            log.info("用户安全退出，用户：{}", loginUser);
            return Result.ok("已安全退出");
        }
        return Result.error(300, "请先登录");
    }
}
