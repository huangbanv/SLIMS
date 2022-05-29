package com.zhangjun.classdesign.slims.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Menu;
import com.zhangjun.classdesign.slims.enums.RoleEnum;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.service.MenuService;
import com.zhangjun.classdesign.slims.util.EntityField;
import com.zhangjun.classdesign.slims.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        if(EntityField.isAdmin()){
            return menuService.save(menu)?Result.ok():Result.error();
        }
        return Result.error(200,"权限不足");
    }

    @DeleteMapping
    public Result deleteMenu(@RequestParam("id")Long id){
        if(EntityField.isAdmin()){
            return menuService.removeById(new Menu().setId(id))?Result.ok():Result.error();
        }
        return Result.error(200,"权限不足");
    }

    @GetMapping
    public Result listMenu(@RequestParam("aimPage")Integer aimPage,
                           @RequestParam("pageSize")Integer pageSize){
        if(EntityField.isAdmin()){
            Page<Menu> menuPage = new Page<>();
            menuPage.setSize(pageSize);
            menuPage.setCurrent(aimPage);
            Page<Menu> page = menuService.page(menuPage);
            return Result.ok().setData(page);
        }
        return Result.error(200,"权限不足");
    }

    @PostMapping
    public Result updateMenu(@RequestBody Menu menu){
        if(EntityField.isAdmin()){
            if(menu.getId()!=null){
                return menuService.updateById(menu)?Result.ok():Result.error();
            }
            return Result.error();
        }
        return Result.error(200,"权限不足");
    }
}

