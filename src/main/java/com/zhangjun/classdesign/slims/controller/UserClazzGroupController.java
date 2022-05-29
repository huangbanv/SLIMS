package com.zhangjun.classdesign.slims.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Clazz;
import com.zhangjun.classdesign.slims.entity.Menu;
import com.zhangjun.classdesign.slims.entity.UserClazzGroup;
import com.zhangjun.classdesign.slims.service.UserClazzGroupService;
import com.zhangjun.classdesign.slims.util.EntityField;
import com.zhangjun.classdesign.slims.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户班级关系 前端控制器
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@RestController
@RequestMapping("/slims/userclazzgroup")
public class UserClazzGroupController {

    @Autowired
    UserClazzGroupService userClazzGroupService;

    @PutMapping
    public Result putUserClazzGroup(@RequestBody UserClazzGroup userClazzGroup){
        if(EntityField.isAdmin()){
            return userClazzGroupService.save(userClazzGroup)?Result.ok():Result.error();
        }
        return Result.error(200,"权限不足");
    }

    @DeleteMapping
    public Result deleteUserClazzGroup(@RequestParam("id")Long id){
        if(EntityField.isAdmin()){
            return  userClazzGroupService.removeById(id)?Result.ok():Result.error();
        }
        return Result.error(200,"权限不足");
    }

    @GetMapping
    public Result listUserClazzGroup(@RequestParam("aimPage")Integer aimPage,
                                     @RequestParam("pageSize")Integer pageSize){
        if(EntityField.isAdmin()){
            Page<UserClazzGroup> userClazzGroup = new Page<>();
            userClazzGroup.setSize(pageSize);
            userClazzGroup.setCurrent(aimPage);
            Page<UserClazzGroup> page = userClazzGroupService.page(userClazzGroup);
            return Result.ok().setData(page);
        }
        return Result.error(200,"权限不足");
    }

    @PostMapping
    public Result updateUserClazzGroup(@RequestBody UserClazzGroup userClazzGroup){
        if(EntityField.isAdmin()){
            return userClazzGroupService.updateById(userClazzGroup)?Result.ok():Result.error();
        }
        return Result.error(200,"权限不足");
    }
}

