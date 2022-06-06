package com.zhangjun.classdesign.slims.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Leave;
import com.zhangjun.classdesign.slims.enums.HttpStatus;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.service.LeaveService;
import com.zhangjun.classdesign.slims.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 张钧
 * @since 2022-05-27
 */
@RestController
@Slf4j
@RequestMapping("/slims/leave")
public class LeaveController {
    
    @Resource
    LeaveService leaveService;
    
    @PutMapping
    public Result putLeave(@RequestBody Leave leave) {
        boolean b;
        try {
            b = leaveService.putLeave(leave);
        } catch (RoleException e) {
            log.error("请假权限出错，用户：{},错误信息：{}", MyInterceptor.threadLocal.get(), e.getMessage());
            return Result.error(HttpStatus.NO_PERMISSION.getCode(), e.getMessage());
        }
        if (b) {
            log.info("请假成功，用户：{},记录：{}", MyInterceptor.threadLocal.get(), leave);
            return Result.ok("申请请假成功");
        }
        log.warn("请假失败，用户：{},记录：{}", MyInterceptor.threadLocal.get(), leave);
        return Result.error("申请请假失败,请检查表单");
    }
    
    @GetMapping
    public Result listLeave(@RequestParam("aimPage") Integer aimPage,
                            @RequestParam("pageSize") Integer pageSize) {
        Page<Leave> page = leaveService.listLeave(aimPage, pageSize);
        if (page.getRecords() == null || page.getRecords().size() == 0) {
            log.warn("查询请假失败，用户：{}", MyInterceptor.threadLocal.get());
            return Result.error("查询请假失败，可能无记录");
        }
        log.info("查询请假成功，用户：{},记录：{}", MyInterceptor.threadLocal.get(), page.getRecords());
        return Result.ok().setData(page);
    }
    
    @GetMapping("/byClazzAndTime")
    public Result listLeaveByClazzAndTime(@RequestParam("aimPage") Integer aimPage,
                                          @RequestParam("pageSize") Integer pageSize,
                                          @RequestParam("clazzId") Integer clazzId,
                                          @RequestParam("startDate") String startDate,
                                          @RequestParam("endDate") String endDate) {
        Page<Leave> page;
        try {
            page = leaveService.listLeaveByClazzAndTime(aimPage, pageSize,clazzId,startDate,endDate);
        }catch (RoleException e){
            log.error("通过班级和时间段查询请假权限出错，用户：{},错误信息：{}", MyInterceptor.threadLocal.get(), e.getMessage());
            return Result.error(HttpStatus.NO_PERMISSION.getCode(), e.getMessage());
        }
        assert page != null;
        if (page.getRecords() == null || page.getRecords().size() == 0) {
            log.warn("通过班级和时间段查询请假失败，用户：{}", MyInterceptor.threadLocal.get());
            return Result.error("通过班级和时间段查询请假失败，可能无记录");
        }
        log.info("通过班级和时间段查询请假成功，用户：{},记录：{}", MyInterceptor.threadLocal.get(), page.getRecords());
        return Result.ok("通过班级和时间段查询请假成功").setData(page);
    }
    
    @PostMapping("/status")
    public Result changeStatus(@RequestParam("id") Integer id,
                               @RequestParam("status") Integer status) {
        boolean b;
        try {
            b = leaveService.changeStatus(id, status);
        } catch (RoleException e) {
            log.error("修改请假单权限出错，用户：{},错误信息：{},请假单id：{},状态：{}"
                    , MyInterceptor.threadLocal.get(), e.getMessage(), id, status);
            return Result.error(HttpStatus.NO_PERMISSION.getCode(), e.getMessage());
        }
        if (b) {
            log.info("修改请假单成功，用户：{},请假单id：{},状态：{}", MyInterceptor.threadLocal.get(), id, status);
            return Result.ok("修改请假单状态成功");
        }
        log.warn("修改请假单失败，用户：{},请假单id：{},状态：{}", MyInterceptor.threadLocal.get(), id, status);
        return Result.error("修改请假单状态失败");
    }
    
    @PostMapping
    public Result updateLeave(@RequestBody Leave leave) {
        boolean b;
        try {
            b = leaveService.updateLeave(leave);
        } catch (RoleException e) {
            log.error("修改请假单权限出错，用户：{},错误信息：{},请假单信息：{}"
                    , MyInterceptor.threadLocal.get(), e.getMessage(), leave);
            return Result.error(HttpStatus.NO_PERMISSION.getCode(), e.getMessage());
        }
        if (b) {
            log.info("修改请假单成功，用户：{},请假单信息：{}", MyInterceptor.threadLocal.get(), leave);
            return Result.ok("修改请假单成功");
        }
        log.warn("修改请假单失败，用户：{},请假单信息：{}", MyInterceptor.threadLocal.get(), leave);
        return Result.error("修改请假单失败");
    }
    
    @DeleteMapping
    public Result deleteLeave(@RequestParam("id") Integer id,
                              @RequestParam("logicalDelete") Integer logicalDelete) {
        boolean b;
        try {
            b = leaveService.deleteLeave(id, logicalDelete);
        } catch (RoleException e) {
            log.error("删除请假单权限出错，用户：{},错误信息：{},删除请假单id:{},是否逻辑删除：{}"
                    , MyInterceptor.threadLocal.get(), e.getMessage(), id, logicalDelete == 1 ? "是" : "否");
            return Result.error(HttpStatus.NO_PERMISSION.getCode(), e.getMessage());
        }
        if (b) {
            log.info("删除请假单成功，用户：{},删除请假单id:{},是否逻辑删除：{}", MyInterceptor.threadLocal.get(), id, logicalDelete == 1 ? "是" : "否");
            return Result.ok("删除或取消请假单成功");
        }
        log.warn("删除请假单失败，用户：{},删除请假单id:{},是否逻辑删除：{}", MyInterceptor.threadLocal.get(), id, logicalDelete == 1 ? "是" : "否");
        return Result.error("删除或取消请假单失败");
    }
}

