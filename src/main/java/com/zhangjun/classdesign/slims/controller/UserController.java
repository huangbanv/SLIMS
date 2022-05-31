package com.zhangjun.classdesign.slims.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Menu;
import com.zhangjun.classdesign.slims.entity.User;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.service.UserService;
import com.zhangjun.classdesign.slims.util.EntityField;
import com.zhangjun.classdesign.slims.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@RestController
@RequestMapping("/slims/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
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
                return Result.ok().setData(map);
            }
        }
        return Result.error();
    }

    @GetMapping("/logout")
    public Result logout(HttpSession session){
        System.out.println("退出登录");
        session.invalidate();
        return Result.ok();
    }

    @PutMapping("/entry")
    public Result entryUser(@RequestBody User user){
        if(!EntityField.roleCheck(MyInterceptor.threadLocal.get().getRoleId(),user.getRoleId())){
            return Result.error();
        }
        userService.create(user);
        return Result.ok();
    }

    @GetMapping("/list")
    public Result listUser(@RequestParam("aimPage")Integer aimPage,
                           @RequestParam("pageSize")Integer pageSize){
        Page<User> page = userService.getPage(aimPage,pageSize);
        if(page == null){
            return Result.error(200,"无数据");
        }
        return Result.ok().setData(page);
    }

    @DeleteMapping
    public Result deleteUser(@RequestParam("id")Long id){
        boolean deleted = userService.delete(id);
        return deleted?Result.ok():Result.error();
    }

    @PostMapping
    public Result updateUser(@RequestBody User user){
        return userService.updateWithRole(user)?Result.ok():Result.error();
    }
}

