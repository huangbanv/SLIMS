package com.zhangjun.classdesign.slims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.*;
import com.zhangjun.classdesign.slims.enums.HttpStatus;
import com.zhangjun.classdesign.slims.enums.LeaveStatusEnum;
import com.zhangjun.classdesign.slims.enums.RoleEnum;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.mapper.ClazzMapper;
import com.zhangjun.classdesign.slims.mapper.LeaveMapper;
import com.zhangjun.classdesign.slims.mapper.UserMapper;
import com.zhangjun.classdesign.slims.service.LeaveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.classdesign.slims.util.Result;
import com.zhangjun.classdesign.slims.util.RoleCheck;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 请假条 服务实现类
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@Service
public class LeaveServiceImpl extends ServiceImpl<LeaveMapper, Leave> implements LeaveService {

    @Resource
    ClazzMapper clazzMapper;

    @Resource
    UserMapper userMapper;

    Map<Integer,String> map = new HashMap<Integer,String>(){{
        put(0,"未批准");
        put(1,"已批准");
        put(2,"已拒绝");
        put(3,"已取消");
        put(4,"已销假");
    }};


    /**
     * 删除和取消（逻辑删除）请假条
     *
     * @param id            请假条id
     * @param logicalDelete 是否逻辑删除 0：否 1：是
     * @return 是否删除成功
     * @throws RoleException 无权限异常
     */
    @Override
    public boolean deleteLeave(Integer id, Integer logicalDelete) throws RoleException {
        Leave leave = getOne(new QueryWrapper<Leave>().eq("id", id));
        if(RoleCheck.isStudent() && RoleCheck.getUser().getId().equals(leave.getStudentId())){
            if(logicalDelete == 1){
                return updateById(leave.setStatus(Integer.valueOf(LeaveStatusEnum.CANCELED.getCode())));
            }else {
                return removeById(id);
            }
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }

    /**
     * 分页查询请假
     *
     * @param aimPage  目标页
     * @param pageSize 页大小
     * @return 请假页
     */
    @Override
    public Page<Leave> listLeave(Integer aimPage, Integer pageSize) {
        QueryWrapper<Leave> leaveQueryWrapper = new QueryWrapper<>();
        if(RoleCheck.isStudent()){
            leaveQueryWrapper.eq("student_id",RoleCheck.getUser().getId());
        }else if(RoleCheck.isCollegeInstructor()){
            leaveQueryWrapper.eq("instructor_id",RoleCheck.getUser().getId()).eq("status",LeaveStatusEnum.NOT_APPROVED.getCode());
        }else if(RoleCheck.isCollegeAdmin()){
            List<Long> userIds = userMapper.selectList(new QueryWrapper<User>().eq("department_id", RoleCheck.getUser().getDepartmentId())).stream().map(User::getId).collect(Collectors.toList());
            leaveQueryWrapper.in("student_id",userIds);
        }
        long count = count(leaveQueryWrapper);
        Page<Leave> leavePage = new Page<Leave>().setTotal(count);
        List<Leave> list = null;
        if(count != 0){
            list = this.list(leaveQueryWrapper.last("limit " + (aimPage - 1) * pageSize + "," + pageSize));
            List<Long> userIds = Stream.of(list.stream().map(Leave::getStudentId).collect(Collectors.toList()), list.stream().map(Leave::getInstructorId).collect(Collectors.toList())).flatMap(Collection::stream).collect(Collectors.toList());
            if(userIds.size() > 0){
                Map<Long, String> names = userMapper.selectList(new QueryWrapper<User>().in("id", userIds)).stream().collect(Collectors.toMap(User::getId, User::getName));
                list.forEach(leave -> {
                    leave.setStudentName(names.get(leave.getStudentId())).setInstructorName(names.get(leave.getInstructorId())).setTypeS(leave.getType()==0?"事假":"病假").setStatusS(map.get(leave.getStatus()));
                });
            }
        }
        return leavePage.setSize(pageSize).setCurrent(aimPage).setRecords(list);
    }

    /**
     * 请假
     *
     * @param leave 请假信息
     * @return 是否请假成功
     * @throws RoleException 无权限异常
     */
    @Override
    public boolean putLeave(Leave leave) throws RoleException {
        if(!RoleCheck.isStudent()){
            throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
        }
        Clazz clazz = clazzMapper.selectOne(new QueryWrapper<Clazz>().eq("id", RoleCheck.getUser().getClazzId()));
        leave.setStudentId(RoleCheck.getUser().getId()).setInstructorId(clazz.getInstructorId()).setDays().setStatus(Integer.valueOf(LeaveStatusEnum.NOT_APPROVED.getCode()));
        if(leave.getDays().signum() != 1){
            return false;
        }
        return save(leave);
    }

    /**
     * 修改请假单信息
     *
     * @param leave 请假单信息
     * @return 是否删除成功
     * @throws RoleException 无权限异常
     */
    @Override
    public boolean updateLeave(Leave leave) throws RoleException {
        Leave oldOne = getOne(new QueryWrapper<Leave>().eq("id", leave.getId()));
        if(RoleCheck.isStudent() && RoleCheck.getUser().getId().equals(oldOne.getStudentId())){

        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }
}
