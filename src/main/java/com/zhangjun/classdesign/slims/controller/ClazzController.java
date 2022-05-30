package com.zhangjun.classdesign.slims.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Clazz;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.service.ClazzService;
import com.zhangjun.classdesign.slims.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 班级信息 前端控制器
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@RestController
@RequestMapping("/slims/clazz")
@Slf4j
public class ClazzController {
    
    @Autowired
    ClazzService clazzService;
    
    @PutMapping
    public Result putClazz(@RequestBody Clazz clazz) {
        boolean b;
        try {
            b = clazzService.putClazz(clazz);
        } catch (RoleException e) {
            log.error("添加班级权限出错，用户：{},班级：{}，错误信息：{}", MyInterceptor.threadLocal.get(), clazz, e.getMessage());
            return Result.error(e.getMessage());
        }
        if (b) {
            log.info("添加班级成功，用户：{},班级：{}", MyInterceptor.threadLocal.get(), clazz);
            return Result.ok();
        } else {
            log.warn("添加班级失败，用户：{},班级：{}", MyInterceptor.threadLocal.get(), clazz);
            return Result.error("添加失败");
        }
    }
    
    @DeleteMapping
    public Result deleteClazz(@RequestParam("id") Long id) {
        boolean b;
        try {
            b = clazzService.deleteClazz(id);
        } catch (RoleException e) {
            log.error("删除班级权限出错，用户：{},班级id：{}，错误信息：{}", MyInterceptor.threadLocal.get(), id, e.getMessage());
            return Result.error(e.getMessage());
        }
        if (b) {
            log.info("删除班级成功，用户：{},班级id：{}", MyInterceptor.threadLocal.get(),id);
            return Result.ok();
        } else {
            log.warn("删除班级失败，用户：{},班级id：{}", MyInterceptor.threadLocal.get(),id);
            return Result.error("删除失败");
        }
    }
    
    @GetMapping
    public Result listClazz(@RequestParam("aimPage") Integer aimPage,
                            @RequestParam("pageSize") Integer pageSize) {
        Page<Clazz> page = clazzService.listClazz(aimPage,pageSize);
        return Result.ok().setData(page);
    }
    
    @PostMapping
    public Result updateClazz(@RequestBody Clazz clazz) {
        boolean b;
        try {
             b = clazzService.updateClazz(clazz);
        } catch (RoleException e) {
            log.error("修改班级权限出错，用户：{},班级：{}，错误信息：{}", MyInterceptor.threadLocal.get(),clazz, e.getMessage());
            return Result.error(e.getMessage());
        }
        if (b) {
            log.info("修改班级成功，用户：{},班级：{}", MyInterceptor.threadLocal.get(),clazz);
            return Result.ok();
        } else {
            log.warn("修改班级失败，用户：{},班级：{}", MyInterceptor.threadLocal.get(),clazz);
            return Result.error("修改失败");
        }
    }
    
}

