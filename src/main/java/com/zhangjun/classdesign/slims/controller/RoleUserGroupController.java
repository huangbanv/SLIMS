package com.zhangjun.classdesign.slims.controller;


import com.zhangjun.classdesign.slims.service.RoleUserGroupService;
import com.zhangjun.classdesign.slims.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 角色用户关系 前端控制器
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@RestController
@RequestMapping("/slims/roleusergroup")
public class RoleUserGroupController {

    @Autowired
    RoleUserGroupService roleUserGroupService;

    @PutMapping
    public Result putMenu(){


        return Result.ok();
    }

    @DeleteMapping
    public Result deleteMenu(){
        return Result.ok();
    }

    @GetMapping
    public Result listMenu(){
        return Result.ok();
    }

    @PostMapping
    public Result updateMenu(){
        return Result.ok();
    }
}

