package com.zhangjun.classdesign.slims.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.RoleUserGroup;
import com.zhangjun.classdesign.slims.service.RoleUserGroupService;
import com.zhangjun.classdesign.slims.util.Result;
import com.zhangjun.classdesign.slims.util.RoleCheck;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 张钧
 * @since 2022-05-27
 */
@RestController
@RequestMapping("/slims/roleusergroup")
public class RoleUserGroupController {

    @Resource
    RoleUserGroupService roleUserGroupService;

    @PutMapping
    public Result putRoleUserGroup(@RequestBody RoleUserGroup roleUserGroup){
        if(RoleCheck.isAdmin()){
            return roleUserGroupService.save(roleUserGroup)?Result.ok():Result.error();
        }
        return Result.error();
    }

    @DeleteMapping
    public Result deleteRoleUserGroup(@RequestParam("id")Long id){
        if(RoleCheck.isAdmin()){
            return roleUserGroupService.removeById(id)?Result.ok():Result.error();
        }
        return Result.error();
    }

    @GetMapping
    public Result listRoleUserGroup(@RequestParam("aimPage")Integer aimPage,
                                    @RequestParam("pageSize")Integer pageSize){
        if(RoleCheck.isAdmin()){
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
        if(RoleCheck.isAdmin()){
            return roleUserGroupService.updateById(roleUserGroup)?Result.ok():Result.error();
        }
        return Result.error();
    }
}

