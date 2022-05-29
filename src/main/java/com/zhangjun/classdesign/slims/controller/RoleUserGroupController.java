package com.zhangjun.classdesign.slims.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.javafx.scene.control.behavior.PaginationBehavior;
import com.zhangjun.classdesign.slims.entity.RoleUserGroup;
import com.zhangjun.classdesign.slims.service.RoleUserGroupService;
import com.zhangjun.classdesign.slims.util.EntityField;
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
    public Result putRoleUserGroup(@RequestBody RoleUserGroup roleUserGroup){
        if(EntityField.isAdmin()){
            return roleUserGroupService.save(roleUserGroup)?Result.ok():Result.error();
        }
        return Result.error();
    }

    @DeleteMapping
    public Result deleteRoleUserGroup(@RequestParam("id")Long id){
        if(EntityField.isAdmin()){
            return roleUserGroupService.removeById(id)?Result.ok():Result.error();
        }
        return Result.error();
    }

    @GetMapping
    public Result listRoleUserGroup(@RequestParam("aimPage")Integer aimPage,
                                    @RequestParam("pageSize")Integer pageSize){
        if(EntityField.isAdmin()){
            Page<RoleUserGroup> roleUserGroupPage = new Page<>();
            roleUserGroupPage.setSize(pageSize);
            roleUserGroupPage.setCurrent(aimPage);
            Page<RoleUserGroup> page = roleUserGroupService.page(roleUserGroupPage);
            return Result.ok().setData(page);
        }
        return Result.error();
    }

    @PostMapping
    public Result updateRoleUserGroup(@RequestBody RoleUserGroup roleUserGroup){
        if(EntityField.isAdmin()){
            return roleUserGroupService.updateById(roleUserGroup)?Result.ok():Result.error();
        }
        return Result.error();
    }
}

