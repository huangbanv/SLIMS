package com.zhangjun.classdesign.slims.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.classdesign.slims.entity.*;
import com.zhangjun.classdesign.slims.enums.HttpStatus;
import com.zhangjun.classdesign.slims.enums.RoleEnum;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.mapper.ClazzMapper;
import com.zhangjun.classdesign.slims.service.ClazzService;
import com.zhangjun.classdesign.slims.service.RoleUserGroupService;
import com.zhangjun.classdesign.slims.service.UserClazzGroupService;
import com.zhangjun.classdesign.slims.service.UserService;
import com.zhangjun.classdesign.slims.util.Result;
import com.zhangjun.classdesign.slims.util.RoleCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 班级信息 服务实现类
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@Service
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {

    @Resource
    UserClazzGroupService userClazzGroupService;

    @Resource
    UserService userService;

    @Resource
    RoleUserGroupService roleUserGroupService;

    /**
     * 获取辅导员Id
     *
     * @param id 学生id
     * @return 辅导员Id
     */
    @Override
    public Long getInstructorId(Long id) {
        UserClazzGroup userClazzGroup = userClazzGroupService.getOne(new QueryWrapper<UserClazzGroup>().eq("student_id", id));
        Clazz clazz = this.getOne(new QueryWrapper<Clazz>().eq("id", userClazzGroup.getClazzId()));
        return clazz.getInstructorId();
    }

    /**
     * 添加班级信息
     *
     * @param clazz 班级信息
     * @return 是否添加成功
     * @throws RoleException 没有权限异常
     */
    @Override
    public boolean putClazz(Clazz clazz) throws RoleException {
        if (RoleCheck.isAdmin()) {
            return save(clazz);
        } else if (RoleCheck.isCollegeAdmin()) {
            User one = userService.getOne(new QueryWrapper<User>().eq("id", clazz.getInstructorId()));
            if (one.getDepartmentId().equals(RoleCheck.getUser().getDepartmentId())) {
                return save(clazz);
            }
        } else if (RoleCheck.isCollegeInstructor()) {
            if (clazz.getInstructorId().equals(RoleCheck.getUser().getId())) {
                return save(clazz);
            }
        } else {
            throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
        }
        return false;
    }

    /**
     * 通过Id删除班级信息
     *
     * @param id 班级Id
     * @return 是否删除成功
     * @throws RoleException 没有权限异常
     */
    @Override
    public boolean deleteClazz(Long id) throws RoleException {
        if (RoleCheck.isAdmin()) {
            return removeById(id);
        } else if (RoleCheck.isCollegeAdmin()) {
            Clazz oldOne = getOne(new QueryWrapper<Clazz>().eq("id", id));
            User one = userService.getOne(new QueryWrapper<User>().eq("id", oldOne.getInstructorId()));
            if (one.getDepartmentId().equals(RoleCheck.getUser().getDepartmentId())) {
                return removeById(id);
            }
        } else if (RoleCheck.isCollegeInstructor()) {
            Clazz oldOne = getOne(new QueryWrapper<Clazz>().eq("id", id));
            if (oldOne.getInstructorId().equals(RoleCheck.getUser().getId())) {
                return removeById(id);
            }
        } else {
            throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
        }
        return false;
    }

    /**
     * 分页查询班级信息
     *
     * @param aimPage  目标页面
     * @param pageSize 页面大小
     * @return 分页查找数据
     * @throws RoleException 无权限异常
     */
    @Override
    public Page<Clazz> listClazz(Integer aimPage, Integer pageSize) throws RoleException {
        Page<Clazz> clazzPage = new Page<>();
        clazzPage.setSize(pageSize);
        clazzPage.setCurrent(aimPage);
        QueryWrapper<Clazz> clazzQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Clazz> queryWrapper = new QueryWrapper<>();
        Map<Long, String> departmentUsers = new HashMap<>();
        if (RoleCheck.isAdmin()) {
            String code = RoleEnum.COLLEGE_INSTRUCTOR.getCode();
            List<Long> userIds = roleUserGroupService.list(new QueryWrapper<RoleUserGroup>().eq("role_id", code)).stream().map(RoleUserGroup::getUserId).collect(Collectors.toList());
            departmentUsers = userService.list(new QueryWrapper<User>().in("id", userIds)).stream().collect(Collectors.toMap(User::getId, User::getName));
        } else if (RoleCheck.isCollegeAdmin()) {
            String departmentId = RoleCheck.getUser().getDepartmentId();
            departmentUsers = userService.list(new QueryWrapper<User>().eq("department_id", departmentId))
                    .stream().collect(Collectors.toMap(User::getId, User::getName));
            List<String> clazzIds = userClazzGroupService.list(new QueryWrapper<UserClazzGroup>().in("instructor_id", departmentUsers.keySet()))
                    .stream().map(UserClazzGroup::getClazzId).collect(Collectors.toList());
            clazzQueryWrapper.in("id", clazzIds);
            queryWrapper.in("id", clazzIds);
        } else if (RoleCheck.isCollegeInstructor()) {
            User user = RoleCheck.getUser();
            departmentUsers.put(user.getId(), user.getName());
            clazzQueryWrapper.eq("instructor_id", user.getId());
            queryWrapper.eq("instructor_id", user.getId());
        } else {
            throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
        }
        clazzQueryWrapper.last("limit " + (aimPage - 1) * pageSize + "," + pageSize);
        List<Clazz> list = this.list(clazzQueryWrapper);
        Map<Long, String> finalDepartmentUsers = departmentUsers;
        list.forEach(clazz -> clazz.setInstructorName(finalDepartmentUsers.get(clazz.getInstructorId())));
        clazzPage.setRecords(list);
        clazzPage.setTotal(this.count(queryWrapper));
        return clazzPage;
    }

    /**
     * 修改班级信息
     *
     * @param clazz 班级信息
     * @return 是否修改成功
     * @throws RoleException 没有权限异常
     */
    @Override
    public boolean updateClazz(Clazz clazz) throws RoleException {
        if (RoleCheck.isAdmin()) {
            return updateById(clazz);
        } else if (RoleCheck.isCollegeAdmin()) {
            Clazz oldOne = getOne(new QueryWrapper<Clazz>().eq("id", clazz.getId()));
            User one = userService.getOne(new QueryWrapper<User>().eq("id", oldOne.getInstructorId()));
            if (one.getDepartmentId().equals(RoleCheck.getUser().getDepartmentId())) {
                return updateById(clazz);
            }
        } else if (RoleCheck.isCollegeInstructor()) {
            Clazz oldOne = getOne(new QueryWrapper<Clazz>().eq("id", clazz.getId()));
            if (oldOne.getInstructorId().equals(RoleCheck.getUser().getId())) {
                return updateById(clazz);
            }
        } else {
            throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
        }
        return false;
    }
}
