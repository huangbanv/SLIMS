package com.zhangjun.classdesign.slims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.*;
import com.zhangjun.classdesign.slims.enums.HttpStatus;
import com.zhangjun.classdesign.slims.enums.RoleEnum;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.mapper.*;
import com.zhangjun.classdesign.slims.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.classdesign.slims.util.EntityField;
import com.zhangjun.classdesign.slims.util.RoleCheck;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    RoleUserGroupMapper roleGroupMapper;

    @Resource
    RoleMenuGroupMapper menuGroupMapper;

    @Resource
    RoleMapper roleMapper;

    @Resource
    MenuMapper menuMapper;

    @Resource
    DepartmentMapper departmentMapper;

    @Resource
    ClazzMapper clazzMapper;

    @Resource
    UserClazzGroupMapper clazzGroupMapper;

    /**
     * 登录验证
     *
     * @param user 用户
     * @return 是否登录
     */
    @Override
    public User getUser(User user) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        User theOne;
        if (user.getId() != null) {
            theOne = this.getOne(userQueryWrapper.eq("id", user.getId()));
        } else if (user.getAccount() != null) {
            theOne = this.getOne(userQueryWrapper.eq("account", user.getAccount()));
        } else {
            return null;
        }
        if (theOne != null) {
            RoleUserGroup userRole = roleGroupMapper.selectOne(new QueryWrapper<RoleUserGroup>().eq("user_id", theOne.getId()));
            if (userRole != null) {
                theOne.setRoleId(userRole.getRoleId());
                List<Long> menuIds = menuGroupMapper.selectList(
                                new QueryWrapper<RoleMenuGroup>().eq("role_id", userRole.getRoleId()))
                        .stream().map(RoleMenuGroup::getMenuId).collect(Collectors.toList());
                if (menuIds.size() > 0) {
                    List<Menu> menus = menuMapper.selectList(new QueryWrapper<Menu>().in("id", menuIds));
                    theOne.setMenus(menus);
                }
                if(theOne.getRoleId().equals(RoleEnum.STUDENT.getCode())){
                    UserClazzGroup userClazzGroup = clazzGroupMapper.selectOne(new QueryWrapper<UserClazzGroup>().eq("student_id", theOne.getId()));
                    if (userClazzGroup != null){
                        theOne.setClazzId(userClazzGroup.getClazzId());
                        Clazz clazz = clazzMapper.selectOne(new QueryWrapper<Clazz>().eq("id", userClazzGroup.getClazzId()));
                        theOne.setClazzName(clazz.getName());
                    }
                }
            }
            return theOne;
        }
        return null;
    }

    /**
     * 创建用户与角色
     *
     * @param user 用户
     */
    private boolean create(User user) {
        String roleId = user.getRoleId();
        RoleUserGroup roleUserGroup = new RoleUserGroup();
        boolean saveUser = this.save(user);
        roleUserGroup.setUserId(user.getId());
        roleUserGroup.setRoleId(roleId);
        boolean saveGroup = roleGroupMapper.insert(roleUserGroup) == 1;
        return saveUser && saveGroup;
    }

    /**
     * 分页查询用户信息
     * SQL语句
     * SELECT * FROM `user`
     * where id in
     * (SELECT ru.user_id from role_user_group ru where ru.role_id in
     * (select role.id from role where role.id > "0" and role.id like "%"))
     * AND department_id > "J00"
     * and department_id like "J%"
     * LIMIT 0,2
     *
     * @param aimPage  目标页面
     * @param pageSize 页面大小
     * @return 用户列表
     */
    public Page<User> getPage(Integer aimPage, Integer pageSize) {
        User user = MyInterceptor.threadLocal.get();
        String departmentId = user.getDepartmentId();
        String roleId = user.getRoleId();
        String s = roleId.replaceFirst("[0-9]", "%");
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<Role>().gt("id", roleId).like("id", s);
        List<String> roleIds = roleMapper.selectList(roleQueryWrapper).stream().map(Role::getId).collect(Collectors.toList());
        if (roleIds.size() <= 0) {
            return null;
        }
        QueryWrapper<RoleUserGroup> roleGroupQueryWrapper = new QueryWrapper<RoleUserGroup>().in("role_id", roleIds);
        List<Long> userIds = roleGroupMapper.selectList(roleGroupQueryWrapper).stream().map(RoleUserGroup::getUserId).collect(Collectors.toList());
        if (userIds.size() <= 0) {
            return null;
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>().in("id", userIds).eq("department_id", departmentId);
        List<User> list = this.list(userQueryWrapper.last("limit " + (aimPage - 1) * pageSize + "," + pageSize));
        if (list.size() <= 0) {
            return null;
        }
        long count = this.count(userQueryWrapper);
        Page<User> page = new Page<>();
        page.setTotal(count);
        page.setRecords(list);
        page.setSize(pageSize);
        page.setCurrent(aimPage);
        return page;
    }

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 是否更新成功
     * @throws RoleException 无权限异常
     */
    @Override
    public boolean updateUser(User user) throws RoleException {
        User oldUser = getUser(user);
        checkRole(oldUser);
        if(user.getRoleId().equals(RoleEnum.COLLEGE_ADMIN.getCode())){
            List<Long> userIds = roleGroupMapper.selectList(new QueryWrapper<RoleUserGroup>().eq("role_id", RoleEnum.COLLEGE_ADMIN.getCode())).stream().map(RoleUserGroup::getUserId).collect(Collectors.toList());
            List<String> departmentIds = list(new QueryWrapper<User>().in("id", userIds)).stream().map(User::getDepartmentId).collect(Collectors.toList());
            for (String departmentId : departmentIds) {
                if(user.getDepartmentId().equals(departmentId)){
                    throw new RoleException("该学院已存在管理员");
                }
            }
        }
        roleGroupMapper.update(new RoleUserGroup().setRoleId(user.getRoleId()), new QueryWrapper<RoleUserGroup>().eq("user_id", user.getId()));
        if (user.getClazzId() != null && oldUser.getRoleId().equals(RoleEnum.STUDENT.getCode()) && oldUser.getRoleId().equals(user.getRoleId())) {
            UserClazzGroup userClazzGroup = clazzGroupMapper.selectOne(new QueryWrapper<UserClazzGroup>().eq("student_id", oldUser.getId()));
            if (userClazzGroup == null) {
                clazzGroupMapper.insert(new UserClazzGroup().setClazzId(user.getClazzId()).setStudentId(user.getId()));
            } else if (!(oldUser.getClazzId().equals(user.getClazzId()))) {
                clazzGroupMapper.updateById(userClazzGroup.setClazzId(user.getClazzId()));
            }
        }
        return updateById(user);
    }

    /**
     * 通过id删除用户
     *
     * @param id 用户id
     * @return 是否删除成功
     * @throws RoleException 无权限异常
     */
    @Override
    public boolean deleteUser(Long id) throws RoleException {
        User user = getUser(new User().setId(id));
        checkRole(user);
        if (user.getRoleId().equals(RoleEnum.STUDENT.getCode())) {
            clazzGroupMapper.delete(new QueryWrapper<UserClazzGroup>().eq("student_id", user.getId()));
        }
        roleGroupMapper.delete(new QueryWrapper<RoleUserGroup>().eq("user_id", user.getId()));
        return removeById(id);
    }

    /**
     * 新增用户
     *
     * @param user 用户信息
     * @return 是否添加成功
     * @throws RoleException 无权限异常
     */
    @Override
    public boolean putUser(User user) throws RoleException {
        checkRole(user);
        if(user.getRoleId().equals(RoleEnum.COLLEGE_ADMIN.getCode())){
            List<Long> roleIds = roleGroupMapper.selectList(new QueryWrapper<RoleUserGroup>().eq("role_id", user.getRoleId())).stream().map(RoleUserGroup::getUserId).collect(Collectors.toList());
            if(roleIds.size()>0){
                List<String> admins = list(new QueryWrapper<User>().in("id", roleIds)).stream().map(User::getDepartmentId).collect(Collectors.toList());
                for (String admin: admins) {
                    if(admin.equals(user.getDepartmentId())){
                        throw new RoleException("本学院已有管理员，不可继续添加");
                    }
                }
            }
        }
        return create(user);
    }

    private void checkRole(User user) throws RoleException {
        if (RoleCheck.isAdmin()) {
        } else if (RoleCheck.isCollegeAdmin()) {
            String[] s = user.getRoleId().split("_");
            String[] s1 = RoleCheck.getUser().getRoleId().split("_");
            if (Integer.parseInt(s[0]) <= Integer.parseInt(s1[0]) || Integer.parseInt(s[1]) != Integer.parseInt(s1[1])) {
                throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
            }
        } else if (RoleCheck.isCollegeInstructor()) {
            if ((!user.getRoleId().equals(RoleEnum.STUDENT.getCode())) || !(user.getDepartmentId().equals(RoleCheck.getUser().getDepartmentId()))) {
                throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
            }
        }else if(RoleCheck.isSADAdmin()){
            if(!user.getDepartmentId().equals(RoleCheck.getUser().getDepartmentId())){
                throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
            }
        }else {
            throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
        }
    }

    /**
     * 分页查询用户
     *
     * @param aimPage  目标页
     * @param pageSize 页大小
     * @return 用户页
     * @throws RoleException 无权限异常
     */
    @Override
    public Page<User> listUser(Integer aimPage, Integer pageSize) throws RoleException {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        User loginUser = RoleCheck.getUser();
        if (RoleCheck.isAdmin()) {
        } else if (RoleCheck.isCollegeAdmin()) {
            userQueryWrapper.eq("department_id", loginUser.getDepartmentId());
        } else if (RoleCheck.isCollegeInstructor()) {
            List<Long> clazzIds = clazzMapper.selectList(new QueryWrapper<Clazz>().eq("instructor_id", loginUser.getId())).stream().map(Clazz::getId).collect(Collectors.toList());
            List<Long> studentIds = clazzGroupMapper.selectList(new QueryWrapper<UserClazzGroup>().in("clazz_id", clazzIds)).stream().map(UserClazzGroup::getStudentId).collect(Collectors.toList());
            userQueryWrapper.in("id", studentIds);
        } else if (RoleCheck.isSADAdmin()) {
            userQueryWrapper.eq("department_id", loginUser.getDepartmentId());
        } else {
            throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
        }
        Page<User> userPage = new Page<User>().setSize(pageSize).setCurrent(aimPage).setTotal(count(userQueryWrapper));
        userQueryWrapper.last("limit " + (aimPage - 1) * pageSize + "," + pageSize);
        List<User> list = list(userQueryWrapper);
        List<Long> ids = list.stream().map(User::getId).collect(Collectors.toList());
        Map<Long, String> userRole = roleGroupMapper.selectList(new QueryWrapper<RoleUserGroup>().in("user_id", ids)).stream().collect(Collectors.toMap(RoleUserGroup::getUserId, RoleUserGroup::getRoleId));
        Map<String, String> roleName = roleMapper.selectList(new QueryWrapper<>()).stream().collect(Collectors.toMap(Role::getId, Role::getName));
        list.forEach(user -> {
            user.setRoleName(roleName.get(userRole.get(user.getId())));
            user.setRoleId(userRole.get(user.getId()));
            if (user.getRoleId().equals(RoleEnum.STUDENT.getCode())) {
                UserClazzGroup userClazzGroup = clazzGroupMapper.selectOne(new QueryWrapper<UserClazzGroup>().eq("student_id", user.getId()));
                if (userClazzGroup != null){
                    Clazz clazz = clazzMapper.selectOne(new QueryWrapper<Clazz>().eq("id", userClazzGroup.getClazzId()));
                    if(clazz != null){
                        user.setClazzId(clazz.getId());
                        user.setClazzName(clazz.getName());
                    }
                }
            }
        });
        setBaseInfo(list);
        return userPage.setRecords(list);
    }

    private void setBaseInfo(List<User> list) {
        Map<String, String> departmentMap = departmentMapper.selectList(new QueryWrapper<>()).stream().collect(Collectors.toMap(Department::getId, Department::getName));
        list.forEach(user -> {
            user.setDepartmentName(departmentMap.get(user.getDepartmentId()));
            user.setStatusS(user.getStatus() == 0 ? "停用" : "正常");
            user.setGenderS(user.getGender() == 0 ? "男" : "女");
        });
    }

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 是否修改成功
     */
    public boolean updateWithRole(User user) {
        User loginUser = MyInterceptor.threadLocal.get();
        String roleId = loginUser.getRoleId();
        user.setAccount(null);
        user.setDepartmentId(null);
        User updateUser = getUser(user);
        if (user.getRoleId() != null && !EntityField.roleCheck(roleId, user.getRoleId())) {
            return false;
        }
        if (!EntityField.roleCheck(roleId, updateUser.getRoleId())) {
            return false;
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        user.setUpdateDate(dateFormat.format(System.currentTimeMillis()));
        return this.updateById(user);
    }

    /**
     * 分页查找辅导员列表
     *
     * @param aimPage  目标页
     * @param pageSize 页大小
     * @return 辅导员列表
     * @throws RoleException 无权限异常
     */
    @Override
    public Page<User> getInstructorList(Integer aimPage, Integer pageSize) throws RoleException {
        if (!RoleCheck.isCollegeAdmin() && !RoleCheck.isAdmin()) {
            throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
        }
        Page<User> userPage = new Page<>();
        List<Long> instructorIds = roleGroupMapper.selectList(new QueryWrapper<RoleUserGroup>()
                        .eq("role_id", RoleEnum.COLLEGE_INSTRUCTOR.getCode()))
                .stream().map(RoleUserGroup::getUserId).collect(Collectors.toList());
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().in("id", instructorIds);
        if(!RoleCheck.isAdmin()){
            queryWrapper.eq("department_id",RoleCheck.getUser().getDepartmentId());
        }
        userPage.setTotal(count(queryWrapper));
        userPage.setSize(pageSize);
        userPage.setCurrent(aimPage);
        List<User> instructors = list(queryWrapper.last("limit " + (aimPage - 1) * pageSize + "," + pageSize));
        setBaseInfo(instructors);
        userPage.setRecords(instructors);
        return userPage;
    }

    /**
     * 更新辅导员信息
     *
     * @param user 辅导员
     * @return 是否更新成功
     * @throws RoleException 无权限异常
     */
    @Override
    public boolean updateInstructor(User user) throws RoleException {
        if (RoleCheck.isCollegeAdmin() || RoleCheck.isAdmin()) {
            return updateById(user);
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }

    /**
     * 新增辅导员信息
     *
     * @param user 辅导员信息
     * @return 是否新增成功
     * @throws RoleException 无权限异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean putInstructor(User user) throws RoleException {
        if (RoleCheck.isCollegeAdmin() || RoleCheck.isAdmin()) {
            user.setRoleId(RoleEnum.COLLEGE_INSTRUCTOR.getCode());
            return create(user);
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }

    /**
     * 查询所有辅导员
     *
     * @return 辅导员列表
     * @throws RoleException 无权限异常
     */
    @Override
    public List<User> listInstructor() throws RoleException {
        if(RoleCheck.isSADAdmin() || RoleCheck.isSADWorker() || RoleCheck.isStudent()){
            throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
        }
        List<Long> userIds = roleGroupMapper.selectList(new QueryWrapper<RoleUserGroup>().eq("role_id", RoleEnum.COLLEGE_INSTRUCTOR.getCode()))
                .stream().map(RoleUserGroup::getUserId).collect(Collectors.toList());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("id", userIds);
        if(RoleCheck.isCollegeInstructor()){
            userQueryWrapper.eq("department_id",RoleCheck.getUser().getDepartmentId());
        }
        return list(userQueryWrapper);
    }

    /**
     * 删除辅导员信息
     *
     * @param id 辅导员id
     * @return 是否删除成功
     * @throws RoleException 无权限异常
     */
    @Override
    public boolean deleteInstructor(Integer id) throws RoleException {
        if (RoleCheck.isCollegeAdmin() || RoleCheck.isAdmin()) {
            return removeById(id);
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }
}
