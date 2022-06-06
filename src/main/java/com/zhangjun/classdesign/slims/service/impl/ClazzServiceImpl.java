package com.zhangjun.classdesign.slims.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.classdesign.slims.entity.*;
import com.zhangjun.classdesign.slims.enums.HttpStatus;
import com.zhangjun.classdesign.slims.enums.RoleEnum;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.mapper.ClazzMapper;
import com.zhangjun.classdesign.slims.mapper.RoleUserGroupMapper;
import com.zhangjun.classdesign.slims.mapper.UserClazzGroupMapper;
import com.zhangjun.classdesign.slims.mapper.UserMapper;
import com.zhangjun.classdesign.slims.service.ClazzService;
import com.zhangjun.classdesign.slims.util.RoleCheck;
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
    UserClazzGroupMapper userClazzGroupMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    RoleUserGroupMapper roleUserGroupMapper;

    /**
     * 获取辅导员Id
     *
     * @param id 学生id
     * @return 辅导员Id
     */
    @Override
    public Long getInstructorId(Long id) {
        UserClazzGroup userClazzGroup = userClazzGroupMapper.selectOne(new QueryWrapper<UserClazzGroup>().eq("student_id", id));
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
            User one = userMapper.selectOne(new QueryWrapper<User>().eq("id", clazz.getInstructorId()));
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
            User one = userMapper.selectOne(new QueryWrapper<User>().eq("id", oldOne.getInstructorId()));
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
     * 通过部门id查询班级信息
     *
     * @param id 部门id
     * @return 分页查找数据
     * @throws RoleException 无权限异常
     */
    @Override
    public List<Clazz> listClazz(String id) throws RoleException {
        QueryWrapper<Clazz> queryWrapper = new QueryWrapper<>();
        if(RoleCheck.isAdmin()){
        }else if (RoleCheck.isCollegeAdmin()){
            List<Long> instructor = userMapper.selectList(new QueryWrapper<User>().eq("department_id",id))
                    .stream().map(User::getId).collect(Collectors.toList());
            queryWrapper.in("instructor_id",instructor);
        }else {
            throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
        }
        return list(queryWrapper);
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
        QueryWrapper<Clazz> clazzQueryWrapper = new QueryWrapper<>();
        Map<Long, String> departmentUsers = new HashMap<>();
        if (RoleCheck.isAdmin()) {
            String code = RoleEnum.COLLEGE_INSTRUCTOR.getCode();
            List<Long> userIds = roleUserGroupMapper.selectList(new QueryWrapper<RoleUserGroup>().eq("role_id", code)).stream().map(RoleUserGroup::getUserId).collect(Collectors.toList());
            departmentUsers = userMapper.selectList(new QueryWrapper<User>().in("id", userIds)).stream().collect(Collectors.toMap(User::getId, User::getName));
        } else if (RoleCheck.isCollegeAdmin()) {
            String departmentId = RoleCheck.getUser().getDepartmentId();
            departmentUsers = userMapper.selectList(new QueryWrapper<User>().eq("department_id", departmentId))
                    .stream().collect(Collectors.toMap(User::getId, User::getName));
            List<Long> clazzIds = userClazzGroupMapper.selectList(new QueryWrapper<UserClazzGroup>().in("instructor_id", departmentUsers.keySet()))
                    .stream().map(UserClazzGroup::getClazzId).collect(Collectors.toList());
            clazzQueryWrapper.in("id", clazzIds);
        } else if (RoleCheck.isCollegeInstructor()) {
            User user = RoleCheck.getUser();
            departmentUsers.put(user.getId(), user.getName());
            clazzQueryWrapper.eq("instructor_id", user.getId());
        } else {
            throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
        }
        Page<Clazz> clazzPage = new Page<Clazz>().setSize(pageSize).setCurrent(aimPage).setTotal(this.count(clazzQueryWrapper));
        List<Clazz> list = this.list(clazzQueryWrapper.last("limit " + (aimPage - 1) * pageSize + "," + pageSize));
        Map<Long, String> finalDepartmentUsers = departmentUsers;
        list.forEach(clazz -> clazz.setInstructorName(finalDepartmentUsers.get(clazz.getInstructorId())));
        return clazzPage.setRecords(list);
    }
    
    /**
     * 查询所有班级名称
     *
     * @return 班级名称
     * @throws RoleException 无权限异常
     */
    @Override
    public List<Clazz> listAllClazz() throws RoleException {
        if(RoleCheck.isStudent()){
            throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
        }
        return list();
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
            User one = userMapper.selectOne(new QueryWrapper<User>().eq("id", oldOne.getInstructorId()));
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
