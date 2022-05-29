package com.zhangjun.classdesign.slims.controller;


import com.zhangjun.classdesign.slims.entity.Clazz;
import com.zhangjun.classdesign.slims.service.UserClazzGroupService;
import com.zhangjun.classdesign.slims.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户班级关系 前端控制器
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@RestController
@RequestMapping("/slims/userclazzgroup")
public class UserClazzGroupController {

    @Autowired
    UserClazzGroupService userClazzGroupService;

    @PutMapping
    public Result putRelation(){


        return Result.ok();
    }

    @DeleteMapping
    public Result deleteRelation(){
        return Result.ok();
    }

    @GetMapping
    public Result listRelation(){
        return Result.ok();
    }

    @PostMapping
    public Result updateRelation(){
        return Result.ok();
    }
}

