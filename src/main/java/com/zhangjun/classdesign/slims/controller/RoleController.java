package com.zhangjun.classdesign.slims.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Role;
import com.zhangjun.classdesign.slims.enums.HttpStatus;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.service.RoleService;
import com.zhangjun.classdesign.slims.util.Result;
import com.zhangjun.classdesign.slims.util.RoleCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 张钧
 * @since 2022-05-27
 */
@RestController
@Slf4j
@RequestMapping("/slims/role")
public class RoleController {

    @Resource
    RoleService roleService;

    @PutMapping
    public Result putRole(@RequestBody Role role){
        boolean b;
        try {
            b = roleService.putRole(role);
        }catch (RoleException e){
            log.error("添加角色权限不足，用户：{},错误信息：{},角色：{}", MyInterceptor.threadLocal.get(), e.getMessage(),role);
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
        }
        if(b){
            log.info("添加角色成功，用户：{},角色：{}", MyInterceptor.threadLocal.get(), role);
            return Result.ok("添加角色成功");
        }
        log.warn("添加菜单失败，用户：{},角色：{}", MyInterceptor.threadLocal.get(),role);
        return Result.error(500,"添加角色错误");
    }

    @DeleteMapping
    public Result deleteRole(@RequestParam("id")String id){
        boolean b;
        try {
            b = roleService.deleteRole(id);
        }catch (RoleException e){
            log.error("删除角色权限出错，用户：{},错误信息：{},角色id：{}", MyInterceptor.threadLocal.get(), e.getMessage(),id);
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
        }
        if(b){
            log.info("删除角色成功，用户：{},角色id：{}", MyInterceptor.threadLocal.get(), id);
            return Result.ok("删除角色成功");
        }
        log.warn("删除角色失败，用户：{},角色id：{}", MyInterceptor.threadLocal.get(),id);
        return Result.error(500,"删除角色错误");
    }

    @GetMapping
    public Result listRole(){
        List<Role> list;
        try {
            list = roleService.listRole();
        }catch (RoleException e){
            log.error("查询角色权限出错，用户：{},错误信息：{}", MyInterceptor.threadLocal.get(), e.getMessage());
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
        }
        log.info("查询角色成功，用户：{},查询结果：{}", MyInterceptor.threadLocal.get(),list);
        return Result.ok("查询角色成功").setData(list);
    }

    @GetMapping("/getRole")
    public Result getRole(){
        return Result.ok().setData(RoleCheck.getUser().getRoleId());
    }

    @PostMapping
    public Result updateRole(@RequestBody Role role){
        if(role.getId()==null){
            log.error("角色信息输入错误，id不得为空，用户：{},角色：{}",MyInterceptor.threadLocal.get(),role);
            return Result.error("角色id不得为空");
        }
        boolean b;
        try {
            b = roleService.updateRole(role);
        }catch (RoleException e){
            log.error("修改角色权限出错，用户：{},错误信息：{},角色：{}", MyInterceptor.threadLocal.get(), e.getMessage(),role);
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
        }
        if(b){
            log.info("修改角色成功，用户：{},角色：{}", MyInterceptor.threadLocal.get(), role);
            return Result.ok("修改角色成功");
        }
        log.warn("修改角色失败，用户：{},角色：{}", MyInterceptor.threadLocal.get(),role);
        return Result.error(500,"修改角色错误");
    }
}

