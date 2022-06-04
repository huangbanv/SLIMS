package com.zhangjun.classdesign.slims.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Menu;
import com.zhangjun.classdesign.slims.entity.User;
import com.zhangjun.classdesign.slims.enums.HttpStatus;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.service.UserService;
import com.zhangjun.classdesign.slims.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 张钧
 * @Description
 * @create 2022-06-02 21:29
 */
@RestController
@Slf4j
@RequestMapping("/slims/instructor")
public class InstructorController {

    @Resource
    UserService userService;

    @GetMapping
    public Result listInstructor(@RequestParam("aimPage")Integer aimPage,
                                 @RequestParam("pageSize")Integer pageSize){
        Page<User> page;
        try {
            page = userService.getInstructorList(aimPage,pageSize);
        } catch (RoleException e) {
            log.error("查询辅导员权限出错，用户：{},错误信息：{}", MyInterceptor.threadLocal.get(), e.getMessage());
            return Result.error(e.getMessage());
        }
        if(page == null){
            log.warn("查询辅导员失败，用户：{}，目标页：{}，页大小：{}", MyInterceptor.threadLocal.get(),aimPage,pageSize);
            return Result.error("无数据");
        }
        log.info("查询辅导员成功，用户：{},记录：{}", MyInterceptor.threadLocal.get(), page);
        return Result.ok().setData(page);
    }

    @GetMapping("/listAll")
    public Result listAllInstructor(){
        List<User> list;
        try {
            list = userService.listAllInstructor();
        } catch (RoleException e) {
            log.error("查询辅导员权限出错，用户：{},错误信息：{}", MyInterceptor.threadLocal.get(), e.getMessage());
            return Result.error(e.getMessage());
        }
        if(list == null){
            log.warn("查询辅导员失败，用户：{}", MyInterceptor.threadLocal.get());
            return Result.error("无数据");
        }
        log.info("查询辅导员成功，用户：{},记录：{}", MyInterceptor.threadLocal.get(), list);
        return Result.ok().setData(list);
    }

    @PutMapping
    public Result putInstructor(@RequestBody User user){
        boolean b;
        try {
            b = userService.putInstructor(user);
        }catch (RoleException e){
            log.error("添加菜单权限出错，用户：{},错误信息：{},辅导员信息：{}", MyInterceptor.threadLocal.get(), e.getMessage(),user);
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
        }
        if(b){
            log.info("成功，用户：{},辅导员信息：{}", MyInterceptor.threadLocal.get(), user);
            return Result.ok("添加菜单成功");
        }
        log.warn("添加辅导员失败，用户：{},辅导员信息：{}", MyInterceptor.threadLocal.get(),user);
        return Result.error(500,"添加辅导员错误");
    }

    @DeleteMapping
    public Result deleteInstructor(@RequestParam("id")Integer id){
        boolean b;
        try {
            b = userService.deleteInstructor(id);
        }catch (RoleException e){
            log.error("删除辅导员权限出错，用户：{},错误信息：{},辅导员id：{}", MyInterceptor.threadLocal.get(), e.getMessage(),id);
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
        }
        if(b){
            log.info("删除辅导员成功，用户：{},辅导员id：{}", MyInterceptor.threadLocal.get(), id);
            return Result.ok("删除辅导员成功");
        }
        log.warn("删除辅导员失败，用户：{},辅导员id：{}", MyInterceptor.threadLocal.get(),id);
        return Result.error(500,"删除辅导员错误");
    }

    @PostMapping
    public Result updateInstructor(@RequestBody User user){
        if(user.getId()==null){
            log.error("辅导员信息输入错误，id不得为空，用户：{},菜单：{}",MyInterceptor.threadLocal.get(),user);
            return Result.error("辅导员id不得为空");
        }
        boolean b;
        try {
            b = userService.updateInstructor(user);
        }catch (RoleException e){
            log.error("修改辅导员权限出错，用户：{},错误信息：{},辅导员：{}", MyInterceptor.threadLocal.get(), e.getMessage(),user);
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
        }
        if(b){
            log.info("修改辅导员成功，用户：{},辅导员：{}", MyInterceptor.threadLocal.get(),user);
            return Result.ok("修改菜单成功");
        }else {
            log.warn("修改辅导员失败，用户：{},辅导员：{}", MyInterceptor.threadLocal.get(),user);
            return Result.error("修改辅导员失败");
        }
    }
}
