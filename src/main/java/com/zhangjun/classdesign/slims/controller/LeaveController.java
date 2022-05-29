package com.zhangjun.classdesign.slims.controller;


import com.zhangjun.classdesign.slims.service.LeaveService;
import com.zhangjun.classdesign.slims.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 请假条 前端控制器
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@RestController
@RequestMapping("/slims/leave")
public class LeaveController {

    @Autowired
    LeaveService leaveService;


    @PutMapping
    public Result putLeave(){


        return Result.ok();
    }

    @DeleteMapping
    public Result deleteLeave(){
        return Result.ok();
    }

    @GetMapping
    public Result listLeave(){
        return Result.ok();
    }

    @PostMapping
    public Result updateLeave(){
        return Result.ok();
    }
}

