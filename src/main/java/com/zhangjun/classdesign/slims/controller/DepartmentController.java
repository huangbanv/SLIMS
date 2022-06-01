package com.zhangjun.classdesign.slims.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Department;
import com.zhangjun.classdesign.slims.entity.User;
import com.zhangjun.classdesign.slims.enums.RoleEnum;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.service.DepartmentService;
import com.zhangjun.classdesign.slims.util.EntityField;
import com.zhangjun.classdesign.slims.util.Result;
import com.zhangjun.classdesign.slims.util.RoleCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * <p>
 * 部门 前端控制器
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@RestController
@RequestMapping("/slims/department")
@Slf4j
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @PutMapping
    public Result putDepartment(@RequestBody Department department){
        boolean b;
        try {
            b = departmentService.putDepartment(department);
        } catch (RoleException e) {
            log.error("添加部门权限出错，用户：{},部门：{}，错误信息：{}", MyInterceptor.threadLocal.get(), department, e.getMessage());
            return Result.error(e.getMessage());
        }
        if (b) {
            log.info("添加部门成功，用户：{},部门：{}", MyInterceptor.threadLocal.get(), department);
            return Result.ok();
        } else {
            log.warn("添加部门失败，用户：{},部门：{}", MyInterceptor.threadLocal.get(), department);
            return Result.error("添加失败");
        }
        
    }

    @DeleteMapping
    public Result deleteDepartment(@RequestParam("id")String id){
        boolean b;
        try {
            b = departmentService.deleteDepartment(id);
        } catch (RoleException e) {
            log.error("删除部门权限出错，用户：{},部门id：{}，错误信息：{}", MyInterceptor.threadLocal.get(), id, e.getMessage());
            return Result.error(e.getMessage());
        }
        if (b) {
            log.info("删除部门成功，用户：{},部门id：{}", MyInterceptor.threadLocal.get(), id);
            return Result.ok();
        } else {
            log.warn("删除部门失败，用户：{},部门id：{}", MyInterceptor.threadLocal.get(), id);
            return Result.error("删除失败");
        }
    }

    @GetMapping
    public Result listDepartment(@RequestParam("aimPage")Integer aimPage,
                                 @RequestParam("pageSize")Integer pageSize){
        Page<Department> result = departmentService.listDepartment(aimPage,pageSize);
        return Result.ok().setData(result);
    }

    @PostMapping
    public Result updateDepartment(@RequestBody Department department){
        if(department.getId()==null){
            log.error("部门信息输入错误，id不得为空，，用户：{},部门：{}",MyInterceptor.threadLocal.get(),department);
            return Result.error("部门id不得为空");
        }
        boolean b;
        try {
            b = departmentService.updateDepartment(department);
        } catch (RoleException e) {
            log.error("更新部门权限出错，用户：{},部门：{}，错误信息：{}", MyInterceptor.threadLocal.get(),department, e.getMessage());
            return Result.error(e.getMessage());
        }
        if (b) {
            log.info("更新部门成功，用户：{},部门：{}", MyInterceptor.threadLocal.get(),department);
            return Result.ok();
        } else {
            log.warn("更新部门失败，用户：{},部门：{}", MyInterceptor.threadLocal.get(),department);
            return Result.error("更新失败");
        }
    }

}

