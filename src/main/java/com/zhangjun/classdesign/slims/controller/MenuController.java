package com.zhangjun.classdesign.slims.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Menu;
import com.zhangjun.classdesign.slims.enums.HttpStatus;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.service.MenuService;
import com.zhangjun.classdesign.slims.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 张钧
 * @since 2022-05-27
 */
@RestController
@Slf4j
@RequestMapping("/slims/menu")
public class MenuController {

    @Resource
    MenuService menuService;

    @PutMapping
    public Result putMenu(@RequestBody Menu menu){
        boolean b;
        try {
            b = menuService.putMenu(menu);
        }catch (RoleException e){
            log.error("添加菜单权限出错，用户：{},错误信息：{},菜单：{}", MyInterceptor.threadLocal.get(), e.getMessage(),menu);
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
        }
        if(b){
            log.info("成功，用户：{},菜单：{}", MyInterceptor.threadLocal.get(), menu);
            return Result.ok("添加菜单成功");
        }
        log.warn("添加菜单失败，用户：{},菜单：{}", MyInterceptor.threadLocal.get(),menu);
        return Result.error(500,"添加菜单错误");
    }

    @DeleteMapping
    public Result deleteMenu(@RequestParam("id")Long id){
        boolean b;
        try {
            b = menuService.deleteMenu(id);
        }catch (RoleException e){
            log.error("添加菜单权限出错，用户：{},错误信息：{},菜单id：{}", MyInterceptor.threadLocal.get(), e.getMessage(),id);
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
        }
        if(b){
            log.info("成功，用户：{},菜单id：{}", MyInterceptor.threadLocal.get(),id);
            return Result.ok("删除菜单成功");
        }
        log.warn("添加菜单失败，用户：{},菜单id：{}", MyInterceptor.threadLocal.get(),id);
        return Result.error(500,"添加菜单错误");
        
    }

    @GetMapping
    public Result listMenu(@RequestParam("aimPage")Integer aimPage,
                           @RequestParam("pageSize")Integer pageSize){
        Page<Menu> page;
        try {
            page = menuService.listMenu(aimPage,pageSize);
        } catch (RoleException e) {
            log.error("查询菜单权限出错，用户：{},错误信息：{}", MyInterceptor.threadLocal.get(), e.getMessage());
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
        }
        if(page.getRecords().size()==0){
            log.warn("查询菜单失败，用户：{},记录：{}", MyInterceptor.threadLocal.get(),page);
            return Result.error("查询菜单失败，可能无记录");
        }
        log.info("查询菜单成功，用户：{},记录：{}", MyInterceptor.threadLocal.get(), page);
        return Result.ok().setData(page);
    }

    @GetMapping("/listAll")
    public Result listAll(){
        List<Menu> list;
        try {
            list = menuService.listAll();
        } catch (RoleException e) {
            log.error("查询菜单权限出错，用户：{},错误信息：{}", MyInterceptor.threadLocal.get(), e.getMessage());
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
        }
        if(list.size()==0){
            log.warn("查询菜单失败，用户：{},记录：{}", MyInterceptor.threadLocal.get(),list);
            return Result.error("查询菜单失败，可能无记录");
        }
        log.info("查询菜单成功，用户：{},记录：{}", MyInterceptor.threadLocal.get(), list);
        return Result.ok().setData(list);
    }

    @PostMapping
    public Result updateMenu(@RequestBody Menu menu){
        if(menu.getId()==null){
            log.error("菜单信息输入错误，id不得为空，用户：{},菜单：{}",MyInterceptor.threadLocal.get(),menu);
            return Result.error("菜单id不得为空");
        }
        boolean b;
        try {
            b = menuService.updateMenu(menu);
        }catch (RoleException e){
            log.error("修改菜单权限出错，用户：{},错误信息：{},菜单：{}", MyInterceptor.threadLocal.get(), e.getMessage(),menu);
            return Result.error(HttpStatus.NO_PERMISSION.getCode(),e.getMessage());
        }
        if(b){
            log.info("修改菜单成功，用户：{},菜单：{}", MyInterceptor.threadLocal.get(),menu);
            return Result.ok("修改菜单成功");
        }else {
            log.warn("修改菜单失败，用户：{},菜单：{}", MyInterceptor.threadLocal.get(),menu);
            return Result.error("修改菜单失败");
        }
    }
}

