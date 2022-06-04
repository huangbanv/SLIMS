package com.zhangjun.classdesign.slims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.*;
import com.zhangjun.classdesign.slims.enums.HttpStatus;
import com.zhangjun.classdesign.slims.enums.RoleEnum;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.mapper.UserMapper;
import com.zhangjun.classdesign.slims.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.classdesign.slims.util.EntityField;
import com.zhangjun.classdesign.slims.util.RoleCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
    RoleUserGroupService roleGroupService;

    @Resource
    RoleMenuGroupService menuGroupService;

    @Resource
    RoleService roleService;

    @Resource
    MenuService menuService;

    @Resource
    DepartmentService departmentService;

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
            RoleUserGroup userRole = roleGroupService.getOne(new QueryWrapper<RoleUserGroup>().eq("user_id", theOne.getId()));
            if (userRole != null) {
                theOne.setRoleId(userRole.getRoleId());
                List<Long> menuIds = menuGroupService.list(
                                new QueryWrapper<RoleMenuGroup>().eq("role_id", userRole.getRoleId()))
                        .stream().map(RoleMenuGroup::getMenuId).collect(Collectors.toList());
                if (menuIds.size() > 0) {
                    List<Menu> menus = menuService.list(new QueryWrapper<Menu>().in("id", menuIds));
                    theOne.setMenus(menus);
                }
            }
            return theOne;
        }
        return null;
    }

    /**
     * 创建用户
     *
     * @param user 用户
     */
    private boolean create(User user) {
        String roleId = user.getRoleId();
        RoleUserGroup roleUserGroup = new RoleUserGroup();
        boolean saveUser = this.save(user);
        roleUserGroup.setUserId(user.getId());
        roleUserGroup.setRoleId(roleId);
        boolean saveGroup = roleGroupService.save(roleUserGroup);
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
    @Override
    public Page<User> getPage(Integer aimPage, Integer pageSize) {
        User user = MyInterceptor.threadLocal.get();
        String departmentId = user.getDepartmentId();
        String roleId = user.getRoleId();
        String s = roleId.replaceFirst("[0-9]", "%");
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<Role>().gt("id", roleId).like("id", s);
        List<String> roleIds = roleService.list(roleQueryWrapper).stream().map(Role::getId).collect(Collectors.toList());
        if (roleIds.size() <= 0) {
            return null;
        }
        QueryWrapper<RoleUserGroup> roleGroupQueryWrapper = new QueryWrapper<RoleUserGroup>().in("role_id", roleIds);
        List<Long> userIds = roleGroupService.list(roleGroupQueryWrapper).stream().map(RoleUserGroup::getUserId).collect(Collectors.toList());
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
     * 删除用户
     *
     * @param id 用户Id
     * @return 是否删除成功
     */
    @Override
    public boolean delete(Long id) {
        User loginUser = MyInterceptor.threadLocal.get();
        String roleId = loginUser.getRoleId();
        User user = getUser(new User().setId(id));
        if (!EntityField.roleCheck(roleId, user.getRoleId())) {
            return false;
        }
        return this.remove(new QueryWrapper<User>().eq("id", id));
    }

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 是否修改成功
     */
    @Override
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
        if (RoleCheck.isCollegeAdmin() || RoleCheck.isAdmin()) {
            Page<User> userPage = new Page<>();
            List<Long> instructorIds = roleGroupService.list(new QueryWrapper<RoleUserGroup>()
                            .eq("role_id", RoleEnum.COLLEGE_INSTRUCTOR.getCode()))
                    .stream().map(RoleUserGroup::getUserId).collect(Collectors.toList());
            QueryWrapper<User> queryWrapper = new QueryWrapper<User>().in("id", instructorIds);
            userPage.setTotal(count(queryWrapper));
            userPage.setSize(pageSize);
            userPage.setCurrent(aimPage);
            List<User> instructors = list(queryWrapper.last("limit " + (aimPage - 1) * pageSize + "," + pageSize));
            Map<String, String> departmentMap = departmentService.list().stream().collect(Collectors.toMap(Department::getId, Department::getName));
            instructors.forEach(instructor -> {
                instructor.setDepartmentName(departmentMap.get(instructor.getDepartmentId()));
                instructor.setStatusS(instructor.getStatus() == 0 ? "停用" : "正常");
                instructor.setGenderS(instructor.getGender() == 0 ? "男" : "女");
            });
            userPage.setRecords(instructors);
            return userPage;
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
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
        if(RoleCheck.isCollegeAdmin() || RoleCheck.isAdmin()){
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
            user.setDepartmentId(RoleCheck.getUser().getDepartmentId());
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
    public List<User> listAllInstructor() throws RoleException {
        if(RoleCheck.isCollegeAdmin() || RoleCheck.isAdmin()){
            List<Long> userIds = roleGroupService.list(new QueryWrapper<RoleUserGroup>().eq("role_id", RoleEnum.COLLEGE_INSTRUCTOR.getCode()))
                    .stream().map(RoleUserGroup::getUserId).collect(Collectors.toList());
            return list(new QueryWrapper<User>().in("id", userIds));
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
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
        if(RoleCheck.isCollegeAdmin() || RoleCheck.isAdmin()){
            return removeById(id);
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }
}
