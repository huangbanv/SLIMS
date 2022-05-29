package com.zhangjun.classdesign.slims.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Department;
import com.zhangjun.classdesign.slims.entity.User;
import com.zhangjun.classdesign.slims.enums.RoleEnum;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.service.DepartmentService;
import com.zhangjun.classdesign.slims.util.EntityField;
import com.zhangjun.classdesign.slims.util.Result;
import com.zhangjun.classdesign.slims.util.RoleCheck;
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
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @PutMapping
    public Result putDepartment(@RequestBody Department department){
        if(RoleCheck.isAdmin()){
            departmentService.save(department);
            return Result.ok();
        }
        return Result.error(200,"权限不足");
    }

    @DeleteMapping
    public Result deleteDepartment(@RequestParam("id")String id){
        if(RoleCheck.isAdmin()){
            departmentService.removeById(id);
            return Result.ok();
        }
        return Result.error(200,"权限不足");
    }

    @GetMapping
    public Result listDepartment(@RequestParam("aimPage")Integer aimPage,
                                 @RequestParam("pageSize")Integer pageSize){
        Page<Department> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(aimPage);
        Page<Department> result = departmentService.page(page);
        return Result.ok().setData(result);
    }

    @PostMapping
    public Result updateDepartment(@RequestBody Department department){
        if(RoleCheck.isAdmin()){
            if(department.getId()!=null){
                departmentService.updateById(department);
                return Result.ok();
            }
            return Result.error();
        }
        return Result.error(200,"权限不足");
    }

}

