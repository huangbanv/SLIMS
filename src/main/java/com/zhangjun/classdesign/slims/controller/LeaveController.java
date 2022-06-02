package com.zhangjun.classdesign.slims.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Leave;
import com.zhangjun.classdesign.slims.entity.User;
import com.zhangjun.classdesign.slims.enums.LeaveStatusEnum;
import com.zhangjun.classdesign.slims.enums.RoleEnum;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.service.ClazzService;
import com.zhangjun.classdesign.slims.service.LeaveService;
import com.zhangjun.classdesign.slims.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 张钧
 * @since 2022-05-27
 */
@RestController
@RequestMapping("/slims/leave")
public class LeaveController {

    @Autowired
    LeaveService leaveService;

    @Autowired
    ClazzService clazzService;


    @PutMapping
    public Result putLeave(@RequestBody Leave leave){
        User user = MyInterceptor.threadLocal.get();
        if(!user.getRoleId().equals(RoleEnum.STUDENT.getCode())){
            return Result.error();
        }
        leave.setStudentUserId(user.getId());
        Long instructorId = clazzService.getInstructorId(user.getId());
        if(instructorId!=null){
            leave.setInstructorUserId(instructorId);
            leave.setStatus(LeaveStatusEnum.NOT_APPROVED.getCode());
            leaveService.save(leave);
            return Result.ok();
        }
        return Result.error();
    }

    @GetMapping
    public Result listLeave(@RequestParam("aimPage")Integer aimPage,
                            @RequestParam("pageSize")Integer pageSize){
        Page<Leave> leavePage = new Page<>();
        leavePage.setSize(pageSize);
        leavePage.setCurrent(aimPage);
        Page<Leave> page = leaveService.page(leavePage, new QueryWrapper<Leave>().eq("instructor_user_id", MyInterceptor.threadLocal.get().getId()));
        return Result.ok().setData(page);
    }

    @GetMapping("/status")
    public Result leaveStatus(){
        User user = MyInterceptor.threadLocal.get();
        if(!user.getRoleId().equals(RoleEnum.STUDENT.getCode())){
            return Result.error();
        }
        Leave leave = leaveService.getOne(new QueryWrapper<Leave>().eq("student_user_id", user.getId()));
        return Result.ok().setData(leave);
    }

    @PostMapping
    public Result updateLeave(@RequestParam("status")String status,
                              @RequestParam("id")Long id){
        User user = MyInterceptor.threadLocal.get();
        if(user.getRoleId().equals(RoleEnum.STUDENT.getCode())){
            if(status.equals(LeaveStatusEnum.CANCELED.getCode())){
                boolean update = leaveService.update(new UpdateWrapper<Leave>().set("status", status).eq("id", id));
                if(update){
                    return Result.ok();
                }
            }else if(status.equals(LeaveStatusEnum.TERMINATED.getCode())){
                Leave leave = leaveService.getOne(new QueryWrapper<Leave>().eq("id", id));
                if(leave.getStatus().equals(LeaveStatusEnum.APPROVED.getCode())){
                    boolean update = leaveService.update(new UpdateWrapper<Leave>().set("status", status).eq("id", id));
                    if(update){
                        return Result.ok();
                    }
                }
                return Result.error();
            }
            return Result.error();
        }else if(user.getRoleId().equals(RoleEnum.COLLEGE_INSTRUCTOR.getCode())){
            Leave leave = leaveService.getOne(new QueryWrapper<Leave>().eq("id", id));
            if(leave.getInstructorUserId().equals(user.getId())){
                if(status.equals(LeaveStatusEnum.APPROVED.getCode())||status.equals(LeaveStatusEnum.REFUSED.getCode())) {
                    boolean update = leaveService.update(new UpdateWrapper<Leave>().set("status", status).eq("id", id));
                    if (update) {
                        return Result.ok();
                    }
                }
            }else {
                return Result.error();
            }
        }
        return Result.error();
    }
}

