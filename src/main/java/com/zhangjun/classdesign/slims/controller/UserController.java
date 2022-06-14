package com.zhangjun.classdesign.slims.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.User;
import com.zhangjun.classdesign.slims.enums.HttpStatus;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.service.ImportService;
import com.zhangjun.classdesign.slims.service.UserService;
import com.zhangjun.classdesign.slims.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Resource
    ImportService importService;

    @PutMapping
    public Result putUser(@RequestBody User user){
        boolean b;
        try {
            b = userService.putUser(user);
        }catch (RoleException e){
            log.error("创建用户权限出错，用户：{},错误信息：{}", MyInterceptor.threadLocal.get(), e.getMessage());
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
        }
        if(b){
            log.info("创建用户成功，用户：{},记录：{}", MyInterceptor.threadLocal.get(),user);
            return Result.ok("添加用户成功");
        }
        log.warn("创建用户失败，用户：{}", MyInterceptor.threadLocal.get());
        return Result.error("添加用户失败");
    }

    @PutMapping("/import")
    public Result importUser(@RequestBody MultipartFile multipartFile){
        boolean b;
        try {
            b = importService.importUsers(multipartFile);
        }catch (RoleException e){
            log.error("导入用户权限出错，用户：{},错误信息：{}", MyInterceptor.threadLocal.get(), e.getMessage());
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
        }
        if(b){
            log.info("导入用户成功，用户：{},记录：{}", MyInterceptor.threadLocal.get(), JSONUtil.toJsonStr(multipartFile));
            return Result.ok("导入用户成功");
        }
        log.warn("导入用户失败，用户：{}", MyInterceptor.threadLocal.get());
        return Result.error("导入用户失败");
    }

    @GetMapping
    public Result listUser(@RequestParam("aimPage")Integer aimPage,
                           @RequestParam("pageSize")Integer pageSize){
        Page<User> page;
        try {
            page = userService.listUser(aimPage,pageSize);
        } catch (RoleException e) {
            log.error("查询用户列表权限出错，用户：{},错误信息：{}", MyInterceptor.threadLocal.get(), e.getMessage());
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
        }
        if(page.getRecords().size()==0){
            log.warn("查询用户列表失败，用户：{}", MyInterceptor.threadLocal.get());
            return Result.error("查询菜单失败，可能无记录");
        }
        log.info("查询用户列表成功，用户：{},记录：{}", MyInterceptor.threadLocal.get(),page.getRecords());
        return Result.ok().setData(page);
    }

    @DeleteMapping
    public Result deleteUser(@RequestParam("id")Long id){
        boolean b;
        try {
            b = userService.deleteUser(id);
        }catch (RoleException e){
            log.error("删除用户权限出错，用户：{},错误信息：{},被删除用户id:{}", MyInterceptor.threadLocal.get(), e.getMessage(),id);
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
        }
        if(b){
            log.info("删除用户成功，用户：{},被删除用户id：{}", MyInterceptor.threadLocal.get(),id);
            return Result.ok("删除用户成功");
        }
        log.warn("删除用户失败，用户：{} ,被删除用户id:{}", MyInterceptor.threadLocal.get(),id);
        return Result.error("删除用户失败");
    }

    @PostMapping
    public Result updateUser(@RequestBody User user){
        boolean b;
        try {
            b = userService.updateUser(user);
        }catch (RoleException e){
            log.error("更新用户权限出错，用户：{},错误信息：{}", MyInterceptor.threadLocal.get(), e.getMessage());
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
        }
        if(b){
            log.info("更新用户成功，用户：{},记录：{}", MyInterceptor.threadLocal.get(),user);
            return Result.ok("更新用户成功");
        }
        log.warn("更新用户失败，用户：{}", MyInterceptor.threadLocal.get());
        return Result.error("更新用户失败");
    }
}

