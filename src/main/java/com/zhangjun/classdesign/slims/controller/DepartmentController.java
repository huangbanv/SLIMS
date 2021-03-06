package com.zhangjun.classdesign.slims.controller;

import com.zhangjun.classdesign.slims.entity.Department;
import com.zhangjun.classdesign.slims.enums.HttpStatus;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.service.DepartmentService;
import com.zhangjun.classdesign.slims.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;


/**
 * @author 张钧
 * @since 2022-05-27
 */
@RestController
@RequestMapping("/slims/department")
@Slf4j
public class DepartmentController {

    @Resource
    DepartmentService departmentService;

    @PutMapping
    public Result putDepartment(@RequestBody Department department){
        boolean b;
        try {
            b = departmentService.putDepartment(department);
        } catch (RoleException e) {
            log.error("添加部门权限出错，用户：{},部门：{}，错误信息：{}", MyInterceptor.threadLocal.get(), department, e.getMessage());
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
        }
        if (b) {
            log.info("添加部门成功，用户：{},部门：{}", MyInterceptor.threadLocal.get(), department);
            return Result.ok("添加部门成功");
        } else {
            log.warn("添加部门失败，用户：{},部门：{}", MyInterceptor.threadLocal.get(), department);
            return Result.error("添加部门失败");
        }
    }

    @DeleteMapping
    public Result deleteDepartment(@RequestParam("id")String id){
        boolean b;
        try {
            b = departmentService.deleteDepartment(id);
        } catch (RoleException e) {
            log.error("删除部门权限出错，用户：{},部门id：{}，错误信息：{}", MyInterceptor.threadLocal.get(), id, e.getMessage());
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
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
    public Result listDepartmentDetail(){
        return Result.ok().setData(departmentService.listDepartmentDetail());
    }
    
    @GetMapping("/list")
    public Result listDepartment(){
        return Result.ok().setData(departmentService.listDepartment());
    }
    
    @PostMapping
    public Result updateDepartment(@RequestBody Department department){
        if(department.getId()==null){
            log.error("部门信息输入错误，id不得为空，用户：{},部门：{}",MyInterceptor.threadLocal.get(),department);
            return Result.error("部门id不得为空");
        }
        boolean b;
        try {
            b = departmentService.updateDepartment(department);
        } catch (RoleException e) {
            log.error("更新部门权限出错，用户：{},部门：{}，错误信息：{}", MyInterceptor.threadLocal.get(),department, e.getMessage());
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
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

