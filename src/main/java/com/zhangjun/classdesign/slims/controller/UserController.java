package com.zhangjun.classdesign.slims.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.User;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.service.UserService;
import com.zhangjun.classdesign.slims.util.EntityField;
import com.zhangjun.classdesign.slims.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * @author 张钧
 * @since 2022-05-27
 */
@RestController
@Slf4j
@RequestMapping("/slims/user")
public class UserController {

    @Resource
    UserService userService;

    @PutMapping
    public Result putUser(@RequestBody User user){
        if(!EntityField.roleCheck(MyInterceptor.threadLocal.get().getRoleId(),user.getRoleId())){
            return Result.error();
        }
//        try {
//            userService.create(user);
//        } catch (RoleException | UnexpectedRollbackException e) {
//            e.printStackTrace();
//        }
        return Result.ok();
    }

    @GetMapping("/list")
    public Result listUser(@RequestParam("aimPage")Integer aimPage,
                           @RequestParam("pageSize")Integer pageSize){
        Page<User> page = userService.getPage(aimPage,pageSize);
        if(page == null){
            return Result.error(500,"无数据");
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

