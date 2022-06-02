package com.zhangjun.classdesign.slims.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Clazz;
import com.zhangjun.classdesign.slims.entity.Menu;
import com.zhangjun.classdesign.slims.entity.User;
import com.zhangjun.classdesign.slims.entity.UserClazzGroup;
import com.zhangjun.classdesign.slims.enums.RoleEnum;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.service.ClazzService;
import com.zhangjun.classdesign.slims.service.UserClazzGroupService;
import com.zhangjun.classdesign.slims.util.EntityField;
import com.zhangjun.classdesign.slims.util.Result;
import com.zhangjun.classdesign.slims.util.RoleCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 张钧
 * @since 2022-05-27
 */
@RestController
@RequestMapping("/slims/userclazzgroup")
public class UserClazzGroupController {

    @Autowired
    UserClazzGroupService userClazzGroupService;

    @Autowired
    ClazzService clazzService;

    @PutMapping
    public Result putUserClazzGroup(@RequestBody UserClazzGroup userClazzGroup){
        Clazz clazz = clazzService.getOne(new QueryWrapper<Clazz>().eq("id", userClazzGroup.getClazzId()));
        User user = MyInterceptor.threadLocal.get();
        if(RoleCheck.isAdmin()||
                ( user.getRoleId().equals(RoleEnum.COLLEGE_INSTRUCTOR.getCode())
                        &&clazz.getDepartmentId().equals(user.getDepartmentId()))){
            return userClazzGroupService.save(userClazzGroup)?Result.ok():Result.error();
        }
        return Result.error(200,"权限不足");
    }

    @DeleteMapping
    public Result deleteUserClazzGroup(@RequestParam("id")Long id){
        UserClazzGroup userClazzGroup = userClazzGroupService.getOne(new QueryWrapper<UserClazzGroup>().eq("id", id));
        Clazz clazz = clazzService.getOne(new QueryWrapper<Clazz>().eq("id", userClazzGroup.getClazzId()));
        User user = MyInterceptor.threadLocal.get();
        if(RoleCheck.isAdmin()||
                ( user.getRoleId().equals(RoleEnum.COLLEGE_INSTRUCTOR.getCode())
                        &&clazz.getDepartmentId().equals(user.getDepartmentId()))){
            return  userClazzGroupService.removeById(id)?Result.ok():Result.error();
        }
        return Result.error(200,"权限不足");
    }

    @GetMapping
    public Result listUserClazzGroup(@RequestParam("aimPage")Integer aimPage,
                                     @RequestParam("pageSize")Integer pageSize){
        User user = MyInterceptor.threadLocal.get();
        if(RoleCheck.isAdmin()){
            Page<UserClazzGroup> userClazzGroup = new Page<>();
            userClazzGroup.setSize(pageSize);
            userClazzGroup.setCurrent(aimPage);
            Page<UserClazzGroup> page = userClazzGroupService.page(userClazzGroup);
            return Result.ok().setData(page);
        }else if(user.getRoleId().equals(RoleEnum.COLLEGE_ADMIN.getCode())){
            String departmentId = user.getDepartmentId();
            Page<UserClazzGroup> userClazzGroup = new Page<>();
            userClazzGroup.setSize(pageSize);
            userClazzGroup.setCurrent(aimPage);
            Page<UserClazzGroup> page = userClazzGroupService.page(userClazzGroup, new QueryWrapper<>());
            return Result.ok().setData(page);
        }
        return Result.error(200,"权限不足");
    }

    @PostMapping
    public Result updateUserClazzGroup(@RequestBody UserClazzGroup userClazzGroup){
        UserClazzGroup oldOne = userClazzGroupService.getOne(new QueryWrapper<UserClazzGroup>().eq("id", userClazzGroup.getId()));
        Clazz clazz = clazzService.getOne(new QueryWrapper<Clazz>().eq("id", oldOne.getClazzId()));
        User user = MyInterceptor.threadLocal.get();
        if(RoleCheck.isAdmin()||
                ( user.getRoleId().equals(RoleEnum.COLLEGE_INSTRUCTOR.getCode())
                        &&clazz.getDepartmentId().equals(user.getDepartmentId()))){
            return userClazzGroupService.updateById(userClazzGroup)?Result.ok():Result.error();
        }
        return Result.error(200,"权限不足");
    }
}

