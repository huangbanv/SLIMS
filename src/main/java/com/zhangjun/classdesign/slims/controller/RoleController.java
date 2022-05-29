package com.zhangjun.classdesign.slims.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Menu;
import com.zhangjun.classdesign.slims.entity.Role;
import com.zhangjun.classdesign.slims.service.RoleService;
import com.zhangjun.classdesign.slims.util.EntityField;
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
    public Result putRole(@RequestBody Role role){
        if(EntityField.isAdmin()){
            return roleService.save(role)?Result.ok():Result.error();
        }
        return Result.error();
    }

    @DeleteMapping
    public Result deleteRole(@RequestParam("id")String id){
        if(EntityField.isAdmin()){
            return roleService.removeById(id)?Result.ok():Result.error();
        }
        return Result.error();
    }

    @GetMapping
    public Result listRole(@RequestParam("aimPage")Integer aimPage,
                           @RequestParam("pageSize")Integer pageSize){
        if(EntityField.isAdmin()){
            Page<Role> rolePage = new Page<>();
            rolePage.setSize(pageSize);
            rolePage.setCurrent(aimPage);
            Page<Role> page = roleService.page(rolePage);
            return Result.ok().setData(page);
        }
        return Result.error();
    }

    @PostMapping
    public Result updateRole(@RequestBody Role role){
        if(EntityField.isAdmin()){
            return roleService.updateById(role)?Result.ok():Result.error();
        }
        return Result.error();
    }
}

