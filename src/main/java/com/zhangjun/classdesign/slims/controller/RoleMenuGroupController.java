package com.zhangjun.classdesign.slims.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Role;
import com.zhangjun.classdesign.slims.entity.RoleMenuGroup;
import com.zhangjun.classdesign.slims.service.RoleMenuGroupService;
import com.zhangjun.classdesign.slims.util.EntityField;
import com.zhangjun.classdesign.slims.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 角色菜单关系 前端控制器
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@RestController
@RequestMapping("/slims/rolemenugroup")
public class RoleMenuGroupController {

    @Autowired
    RoleMenuGroupService roleMenuGroupService;

    @PutMapping
    public Result putRoleMenuGroup(@RequestBody RoleMenuGroup roleMenuGroup){
        if(EntityField.isAdmin()){
            return roleMenuGroupService.save(roleMenuGroup)?Result.ok():Result.error();
        }
        return Result.error();
    }

    @DeleteMapping
    public Result deleteRoleMenuGroup(@RequestParam("id")Long id){
        if(EntityField.isAdmin()){
            return roleMenuGroupService.removeById(id)?Result.ok():Result.error();
        }
        return Result.error();
    }

    @GetMapping
    public Result listRoleMenuGroup(@RequestParam("aimPage")Integer aimPage,
                                    @RequestParam("pageSize")Integer pageSize){
        if(EntityField.isAdmin()){
            Page<RoleMenuGroup> roleMenuGroupPage = new Page<>();
            roleMenuGroupPage.setSize(pageSize);
            roleMenuGroupPage.setCurrent(aimPage);
            Page<RoleMenuGroup> page = roleMenuGroupService.page(roleMenuGroupPage);
            return Result.ok().setData(page);
        }
        return Result.error();
    }

    @PostMapping
    public Result updateRoleMenuGroup(@RequestBody RoleMenuGroup roleMenuGroup){
        if(EntityField.isAdmin()){
            return roleMenuGroupService.updateById(roleMenuGroup)?Result.ok():Result.error();
        }
        return Result.error();
    }
}

