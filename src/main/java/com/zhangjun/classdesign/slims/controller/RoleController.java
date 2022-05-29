package com.zhangjun.classdesign.slims.controller;


import com.zhangjun.classdesign.slims.service.RoleService;
import com.zhangjun.classdesign.slims.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@RestController
@RequestMapping("/slims/role")
public class RoleController {

    @Autowired
    RoleService roleService;

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

