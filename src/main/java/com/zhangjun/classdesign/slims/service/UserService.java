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
     * 登录验证
     * @param user 用户
     * @return 是否登录
     */
    User getUser(User user);


    /**
     * 分页查询用户信息
     * @param aimPage 目标页面
     * @param pageSize 页面大小
     * @return 用户列表
     */
    Page<User> getPage(Integer aimPage, Integer pageSize);

    /**
     * 删除用户
     * @param id 用户Id
     * @return 是否删除成功
     */
    boolean delete(Long id);

    /**
     * 修改用户信息
     * @param user 用户信息
     * @return 是否修改成功
     */
    boolean updateWithRole(User user);

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
    List<User> listAllInstructor() throws RoleException;
}
