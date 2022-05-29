package com.zhangjun.classdesign.slims.controller;


import com.zhangjun.classdesign.slims.entity.Clazz;
import com.zhangjun.classdesign.slims.service.ClazzService;
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


        return Result.ok();
    }

    @DeleteMapping
    public Result deleteClazz(){
        return Result.ok();
    }

    @GetMapping
    public Result listClazz(){
        return Result.ok();
    }

    @PostMapping
    public Result updateClazz(){
        return Result.ok();
    }

}

