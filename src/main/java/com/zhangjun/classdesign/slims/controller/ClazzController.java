package com.zhangjun.classdesign.slims.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Clazz;
import com.zhangjun.classdesign.slims.entity.User;
import com.zhangjun.classdesign.slims.enums.RoleEnum;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.service.ClazzService;
import com.zhangjun.classdesign.slims.util.EntityField;
import com.zhangjun.classdesign.slims.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 班级信息 前端控制器
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@RestController
@RequestMapping("/slims/clazz")
public class ClazzController {

    @Autowired
    ClazzService clazzService;

    @PutMapping
    public Result putClazz(@RequestBody Clazz clazz){
        if(EntityField.isAdmin()){
            return clazzService.save(clazz)?Result.ok():Result.error();
        }else {
            User user = MyInterceptor.threadLocal.get();
            if(user.getRoleId().equals(RoleEnum.COLLEGE_ADMIN.getCode())){
                if(clazz.getDepartmentId().equals(user.getDepartmentId())){
                    return clazzService.save(clazz)?Result.ok():Result.error();
                }
            }
            return Result.error();
        }
    }

    @DeleteMapping
    public Result deleteClazz(@RequestParam("id")Long id){
        if(EntityField.isAdmin()){
            return clazzService.removeById(new Clazz().setId(id))?Result.ok():Result.error();
        }else {
            User user = MyInterceptor.threadLocal.get();
            if(user.getRoleId().equals(RoleEnum.COLLEGE_ADMIN.getCode())){
                Clazz old = clazzService.getOne(new QueryWrapper<Clazz>().eq("id",id));
                if(user.getDepartmentId().equals(old.getDepartmentId())){
                    return clazzService.removeById(new Clazz().setId(id))?Result.ok():Result.error();
                }
            }
        }
        return Result.error();
    }

    @GetMapping
    public Result listClazz(@RequestParam("aimPage")Integer aimPage,
                            @RequestParam("pageSize")Integer pageSize){
        Page<Clazz> clazzPage = new Page<>();
        clazzPage.setSize(pageSize);
        clazzPage.setCurrent(aimPage);
        Page<Clazz> page = clazzService.page(clazzPage);
        return Result.ok().setData(page);
    }

    @PostMapping
    public Result updateClazz(@RequestBody Clazz clazz){
        if(EntityField.isAdmin()){
            return clazzService.updateById(clazz)?Result.ok():Result.error();
        }else {
            User user = MyInterceptor.threadLocal.get();
            if(user.getRoleId().equals(RoleEnum.COLLEGE_ADMIN.getCode())){
                Clazz old = clazzService.getOne(new QueryWrapper<Clazz>().eq("id", clazz.getId()));
                if(user.getDepartmentId().equals(old.getDepartmentId())){
                    return clazzService.updateById(clazz)?Result.ok():Result.error();
                }
            }
        }
        return Result.error();
    }

}

