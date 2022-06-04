package com.zhangjun.classdesign.slims.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjun.classdesign.slims.exception.ExistInstructorException;
import com.zhangjun.classdesign.slims.exception.RoleException;

import java.util.List;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
public interface UserService extends IService<User> {

    /**
     * 获取用户
     * @param user 用户
     * @return 获取到的用户信息
     */
    User getUser(User user);

    /**
     * 分页查找辅导员列表
     * @param aimPage 目标页
     * @param pageSize 页大小
     * @return 辅导员列表
     * @throws RoleException 无权限异常
     */
    Page<User> getInstructorList(Integer aimPage, Integer pageSize) throws RoleException;

    /**
     * 更新辅导员信息
     * @param user 辅导员
     * @return 是否更新成功
     * @throws RoleException 无权限异常
     */
    boolean updateInstructor(User user) throws RoleException;

    /**
     * 新增辅导员信息
     * @param user 辅导员信息
     * @return 是否新增成功
     * @throws RoleException 无权限异常
     */
    boolean putInstructor(User user) throws RoleException;

    /**
     * 删除辅导员信息
     * @param id 辅导员id
     * @return 是否删除成功
     * @throws RoleException 无权限异常
     */
    boolean deleteInstructor(Integer id) throws RoleException;

    /**
     * 查询所有辅导员
     * @return 辅导员列表
     * @throws RoleException 无权限异常
     */
    List<User> listInstructor() throws RoleException;

    /**
     * 分页查询用户
     * @param aimPage 目标页
     * @param pageSize 页大小
     * @return 用户页
     * @throws RoleException 无权限异常
     */
    Page<User> listUser(Integer aimPage, Integer pageSize) throws RoleException;

    /**
     * 新增用户
     * @param user 用户信息
     * @return 是否添加成功
     * @throws RoleException 无权限异常
     */
    boolean putUser(User user) throws RoleException;

    /**
     * 通过id删除用户
     * @param id 用户id
     * @return 是否删除成功
     * @throws RoleException 无权限异常
     */
    boolean deleteUser(Long id) throws RoleException;

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 是否更新成功
     * @throws RoleException 无权限异常
     */
    boolean updateUser(User user) throws RoleException;
}
