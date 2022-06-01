package com.zhangjun.classdesign.slims.controller;


import com.alibaba.druid.sql.visitor.functions.Lcase;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Menu;
import com.zhangjun.classdesign.slims.enums.RoleEnum;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.service.MenuService;
import com.zhangjun.classdesign.slims.util.EntityField;
import com.zhangjun.classdesign.slims.util.Result;
import com.zhangjun.classdesign.slims.util.RoleCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单 前端控制器
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@RestController
@RequestMapping("/slims/menu")
public class MenuController {

    @Autowired
    MenuService menuService;

    @PutMapping
    public Result putMenu(@RequestBody Menu menu){
        if(RoleCheck.isAdmin()){
            return menuService.save(menu)?Result.ok():Result.error();
        }
        return Result.error(200,"权限不足");
    }

    @DeleteMapping
    public Result deleteMenu(@RequestParam("id")Long id){
        if(RoleCheck.isAdmin()){
            return menuService.removeById(new Menu().setId(id))?Result.ok():Result.error();
        }
        return Result.error(200,"权限不足");
    }

    @GetMapping
    public Result listMenu(@RequestParam("aimPage")Integer aimPage,
                           @RequestParam("pageSize")Integer pageSize){
        Page<Menu> page;
        try {
            page = menuService.listMenu(aimPage,pageSize);
        } catch (RoleException e) {
            return Result.error(e.getMessage());
        }
        if(page.getRecords() == null||page.getRecords().size()==0){
            return Result.error("无记录");
        }
        return Result.ok().setData(page);
    }

    @PostMapping
    public Result updateMenu(@RequestBody Menu menu){
        if(RoleCheck.isAdmin()){
            if(menu.getId()!=null){
                return menuService.updateById(menu)?Result.ok():Result.error();
            }
            return Result.error();
        }
        return Result.error(200,"权限不足");
    }
}

