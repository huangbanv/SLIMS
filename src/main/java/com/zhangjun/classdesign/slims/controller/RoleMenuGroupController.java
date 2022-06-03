package com.zhangjun.classdesign.slims.controller;

import com.zhangjun.classdesign.slims.entity.Menu;
import com.zhangjun.classdesign.slims.entity.RoleMenuGroup;
import com.zhangjun.classdesign.slims.enums.HttpStatus;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.service.RoleMenuGroupService;
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
@RequestMapping("/slims/rolemenugroup")
public class RoleMenuGroupController {

    @Resource
    RoleMenuGroupService roleMenuGroupService;

    @PutMapping
    public Result putRoleMenuGroup(@RequestBody RoleMenuGroup roleMenuGroup){
        boolean b;
        try {
            b = roleMenuGroupService.putRoleMenuGroup(roleMenuGroup);
        }catch (RoleException e){
            log.error("添加角色菜单权限出错，用户：{},错误信息：{},菜单：{}", MyInterceptor.threadLocal.get(), e.getMessage(),roleMenuGroup);
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
        }
        if(b){
            log.info("添加角色菜单成功，用户：{},菜单：{}", MyInterceptor.threadLocal.get(), roleMenuGroup);
            return Result.ok("添加角色菜单成功");
        }
        log.warn("添加角色菜单失败，用户：{},菜单：{}", MyInterceptor.threadLocal.get(),roleMenuGroup);
        return Result.error(500,"添加角色菜单错误,或已存在");
    }

    @DeleteMapping
    public Result deleteRoleMenuGroup(@RequestParam("id")Long id){
        boolean b;
        try {
            b = roleMenuGroupService.deleteRoleMenuById(id);
        } catch (RoleException e) {
            log.error("删除角色菜单权限出错，用户：{},错误信息：{}", MyInterceptor.threadLocal.get(), e.getMessage());
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
        }
        if(b){
            log.info("删除角色菜单成功，用户：{},角色菜单id：{}", MyInterceptor.threadLocal.get(), id);
            return Result.ok("删除角色菜单成功");
        }
        log.warn("删除角色菜单失败，用户：{},角色菜单id:{}", MyInterceptor.threadLocal.get(),id);
        return Result.error("删除角色菜单失败");
    }

    @GetMapping
    public Result listMenusByRoleId(@RequestParam("id")String id){
        List<Menu> list;
        try {
            list = roleMenuGroupService.listMenusByRoleId(id);
        } catch (RoleException e) {
            log.error("查询角色菜单权限出错，用户：{},错误信息：{}", MyInterceptor.threadLocal.get(), e.getMessage());
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
        }
        if(list == null || list.size()==0){
            log.warn("查询角色菜单失败，用户：{},角色Id:{}", MyInterceptor.threadLocal.get(),id);
            return Result.error("查询菜单失败，可能无记录");
        }
        log.info("查询角色菜单成功，用户：{},记录：{}", MyInterceptor.threadLocal.get(), list);
        return Result.ok().setData(list);
    }
}

