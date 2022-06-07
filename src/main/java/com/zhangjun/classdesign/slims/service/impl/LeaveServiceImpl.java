package com.zhangjun.classdesign.slims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.*;
import com.zhangjun.classdesign.slims.enums.HttpStatus;
import com.zhangjun.classdesign.slims.enums.LeaveStatusEnum;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.mapper.ClazzMapper;
import com.zhangjun.classdesign.slims.mapper.LeaveMapper;
import com.zhangjun.classdesign.slims.mapper.UserMapper;
import com.zhangjun.classdesign.slims.service.LeaveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.classdesign.slims.util.RoleCheck;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
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

    Map<Integer, String> map = new HashMap<Integer, String>() {{
        put(0, "未批准");
        put(1, "已批准");
        put(2, "已拒绝");
        put(3, "已取消");
        put(4, "已销假");
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
        if (RoleCheck.isStudent() && RoleCheck.getUser().getId().equals(leave.getStudentId())) {
            if (logicalDelete == 1) {
                return updateById(leave.setStatus(LeaveStatusEnum.CANCELED.getCode()));
            } else {
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
        if (RoleCheck.isStudent()) {
            leaveQueryWrapper.eq("student_id", RoleCheck.getUser().getId());
        } else if (RoleCheck.isCollegeInstructor()) {
            leaveQueryWrapper.eq("instructor_id", RoleCheck.getUser().getId()).eq("status", LeaveStatusEnum.NOT_APPROVED.getCode());
        } else if (RoleCheck.isCollegeAdmin()) {
            List<Long> userIds = userMapper.selectList(new QueryWrapper<User>().eq("department_id", RoleCheck.getUser().getDepartmentId())).stream().map(User::getId).collect(Collectors.toList());
            leaveQueryWrapper.in("student_id", userIds);
        }
        long count = count(leaveQueryWrapper);
        Page<Leave> leavePage = new Page<Leave>().setTotal(count);
        List<Leave> list = fillFields(count, leaveQueryWrapper, aimPage, pageSize);
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
        if (!RoleCheck.isStudent()) {
            throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
        }
        Clazz clazz = clazzMapper.selectOne(new QueryWrapper<Clazz>().eq("id", RoleCheck.getUser().getClazzId()));
        leave.setStudentId(RoleCheck.getUser().getId()).setInstructorId(clazz.getInstructorId()).setDays().setStatus(LeaveStatusEnum.NOT_APPROVED.getCode());
        if (leave.getDays().signum() != 1) {
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
        System.out.println(oldOne.getStudentId());
        if (RoleCheck.isStudent() && RoleCheck.getUser().getId().equals(oldOne.getStudentId())) {
            if (leave.getStatus().equals(LeaveStatusEnum.NOT_APPROVED.getCode()) || leave.getStatus().equals(LeaveStatusEnum.REFUSED.getCode()) || leave.getStatus().equals(LeaveStatusEnum.CANCELED.getCode())) {
                return updateById(leave.setStatus(LeaveStatusEnum.NOT_APPROVED.getCode()).setDays());
            }
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }

    /**
     * 根据班级和时间段查询请假情况
     *
     * @param aimPage   目标页
     * @param pageSize  页大小
     * @param clazzId   班级Id
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 请假情况
     * @throws RoleException 无权限异常
     */
    @Override
    public Page<Leave> listLeaveByClazzAndTime(Integer aimPage, Integer pageSize, Integer clazzId, String startDate, String endDate) throws RoleException {
        if (RoleCheck.isStudent()) {
            throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
        }
        Long instructorId = clazzMapper.selectOne(new QueryWrapper<Clazz>().eq("id", clazzId)).getInstructorId();
        QueryWrapper<Leave> leaveQueryWrapper = new QueryWrapper<Leave>().eq("instructor_id", instructorId).gt("start_time", startDate).lt("end_time", endDate);
        long count = count(leaveQueryWrapper);
        List<Leave> list = fillFields(count, leaveQueryWrapper, aimPage, pageSize);
        return new Page<Leave>().setSize(pageSize).setCurrent(aimPage).setTotal(count).setRecords(list);
    }

    /**
     * 更新请假单状态
     *
     * @param id     请假单id
     * @param status 状态
     * @return 是否修改成功
     * @throws RoleException 无权限异常
     */
    @Override
    public boolean changeStatus(Integer id, Integer status) throws RoleException {
        final Leave leave = getOne(new QueryWrapper<Leave>().eq("id", id));
        if (LeaveStatusEnum.APPROVED.getCode().equals(status) || LeaveStatusEnum.REFUSED.getCode().equals(status)) {
            if (RoleCheck.getUser().getId().equals(leave.getInstructorId())) {
                return update(new UpdateWrapper<Leave>().set("status", status).eq("id", id));
            }
        } else if (LeaveStatusEnum.TERMINATED.getCode().equals(status)) {
            if (RoleCheck.getUser().getId().equals(leave.getStudentId())) {
                return update(new UpdateWrapper<Leave>().set("status", status).eq("id", id));
            }
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }

    private List<Leave> fillFields(Long count, QueryWrapper<Leave> leaveQueryWrapper, Integer aimPage, Integer pageSize) {
        if (count != 0) {
            if (aimPage != 0) {
                leaveQueryWrapper.last("limit " + (aimPage - 1) * pageSize + "," + pageSize);
            }
            List<Leave> list = list(leaveQueryWrapper);
            List<Long> userIds = Stream.of(list.stream().map(Leave::getStudentId).collect(Collectors.toList()), list.stream().map(Leave::getInstructorId).collect(Collectors.toList())).flatMap(Collection::stream).collect(Collectors.toList());
            if (userIds.size() > 0) {
                Map<Long, String> names = userMapper.selectList(new QueryWrapper<User>().in("id", userIds)).stream().collect(Collectors.toMap(User::getId, User::getName));
                list.forEach(leave -> leave.setStudentName(names.get(leave.getStudentId())).setInstructorName(names.get(leave.getInstructorId())).setTypeS(leave.getType() == 0 ? "事假" : "病假").setStatusS(map.get(leave.getStatus())));
            }
            return list;
        }
        return null;
    }
}
