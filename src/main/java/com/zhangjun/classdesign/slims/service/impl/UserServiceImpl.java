package com.zhangjun.classdesign.slims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Role;
import com.zhangjun.classdesign.slims.entity.RoleMenuGroup;
import com.zhangjun.classdesign.slims.entity.RoleUserGroup;
import com.zhangjun.classdesign.slims.entity.User;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.mapper.UserMapper;
import com.zhangjun.classdesign.slims.service.RoleMenuGroupService;
import com.zhangjun.classdesign.slims.service.RoleService;
import com.zhangjun.classdesign.slims.service.RoleUserGroupService;
import com.zhangjun.classdesign.slims.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.classdesign.slims.util.EntityField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.soap.SOAPBinding;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    RoleUserGroupService roleGroupService;

    @Autowired
    RoleMenuGroupService menuGroupService;

    @Autowired
    RoleService roleService;


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
        if(user.getId()!=null){
            System.out.println(user.getId());
            theOne = this.getOne(userQueryWrapper.eq("id", user.getId()));
            System.out.println(theOne);
        }else if(user.getAccount()!=null){
            theOne = this.getOne(userQueryWrapper.eq("account", user.getAccount()));
        }else {
            return null;
        }
        if (theOne != null) {
            if (theOne.getStatus() == 1) {
                RoleUserGroup userRole = roleGroupService.getOne(new QueryWrapper<RoleUserGroup>().eq("user_id", theOne.getId()));
                theOne.setRoleId(userRole.getRoleId());
                Map<String, Integer> menuMap = menuGroupService.list(
                                new QueryWrapper<RoleMenuGroup>().eq("role_id", userRole.getRoleId()))
                        .stream().collect(Collectors.toMap(menu -> menu.getMenuId().toString(), RoleMenuGroup::getPermissions));
                theOne.setMenus(menuMap);
                return theOne;
            }
        }
        return null;
    }

    /**
     * 创建用户
     *
     * @param user 用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(User user) {
        String roleId = user.getRoleId();
        RoleUserGroup roleUserGroup = new RoleUserGroup();
        roleUserGroup.setUserId(user.getId());
        roleUserGroup.setRoleId(roleId);
        roleGroupService.save(roleUserGroup);
        this.save(user);
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
     * @param aimPage      目标页面
     * @param pageSize     页面大小
     * @return 用户列表
     */
    @Override
    public Page<User> getPage(Integer aimPage, Integer pageSize) {
        User user = MyInterceptor.threadLocal.get();
        String departmentId = user.getDepartmentId();
        String roleId = user.getRoleId();
        String s = roleId.replaceFirst("[0-9]", "%");
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<Role>().gt("id", roleId).like("id",s );
        List<String> roleIds = roleService.list(roleQueryWrapper).stream().map(Role::getId).collect(Collectors.toList());
        if(roleIds.size()<=0){
            return null;
        }
        QueryWrapper<RoleUserGroup> roleGroupQueryWrapper = new QueryWrapper<RoleUserGroup>().in("role_id",roleIds);
        List<Long> userIds = roleGroupService.list(roleGroupQueryWrapper).stream().map(RoleUserGroup::getUserId).collect(Collectors.toList());
        if(userIds.size()<=0){
            return null;
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>().in("id",userIds).eq("department_id", departmentId);
        List<User> list = this.list(userQueryWrapper.last("limit "+(aimPage-1)*pageSize+","+pageSize));
        if(list.size()<=0){
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
     * @param id     用户Id
     * @return 是否删除成功
     */
    @Override
    public boolean delete(Long id) {
        User loginUser = MyInterceptor.threadLocal.get();
        String roleId = loginUser.getRoleId();
        User user = getUser(new User().setId(id));
        if(!EntityField.roleCheck(roleId,user.getRoleId())){
            return false;
        }
        return this.remove(new QueryWrapper<User>().eq("id",id));
    }

    /**
     * 修改用户信息
     *
     * @param user   用户信息
     * @return 是否修改成功
     */
    @Override
    public boolean updateWithRole(User user) {
        User loginUser = MyInterceptor.threadLocal.get();
        String roleId  = loginUser.getRoleId();
        user.setSuperAdmin(null);
        user.setAccount(null);
        user.setDepartmentId(null);
        User updateUser = getUser(user);
        if(user.getRoleId()!=null && !EntityField.roleCheck(roleId,user.getRoleId())){
            return false;
        }
        if(!EntityField.roleCheck(roleId,updateUser.getRoleId())){
            return false;
        }
        user.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        return this.updateById(user);
    }
}
