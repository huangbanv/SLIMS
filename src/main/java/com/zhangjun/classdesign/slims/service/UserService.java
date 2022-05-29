package com.zhangjun.classdesign.slims.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

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
     * 创建用户
     * @param user 用户
     */
    void create(User user);

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
}
