package com.zhangjun.classdesign.slims.controller;

import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.service.ExportService;
import com.zhangjun.classdesign.slims.util.Result;
import com.zhangjun.classdesign.slims.util.RoleCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 张钧
 */
@RestController
@RequestMapping("/slims/export")
@Slf4j
public class ExportController {
    
    @Resource
    ExportService exportService;

    @GetMapping
    public Result getAll(ModelMap map,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        exportService.getAll(map,request,response);
        log.info("已导出所有请假单 导出人：{}", RoleCheck.getUser());
        return Result.ok();
    }

    @GetMapping("/byCondition")
    public Result getByCondition(ModelMap map,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestParam("aimPage") Integer aimPage,
                                 @RequestParam("pageSize") Integer pageSize,
                                 @RequestParam("clazzId") Integer clazzId,
                                 @RequestParam("startDate") String startDate,
                                 @RequestParam("endDate") String endDate) {
        try {
            exportService.getByCondition(map,request,response,aimPage,pageSize,clazzId,startDate,endDate);
        } catch (RoleException e) {
            e.printStackTrace();
        }
        log.info("已分页导出请假单 导出人：{}，目标页：{}，页大小：{}，班级id:{},开始时间：{}，结束时间：{}",
                RoleCheck.getUser(),aimPage,pageSize,clazzId,startDate,endDate);
        return Result.ok();
    }
}
