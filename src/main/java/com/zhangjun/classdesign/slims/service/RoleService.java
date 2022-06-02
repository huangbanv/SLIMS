package com.zhangjun.classdesign.slims.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjun.classdesign.slims.exception.RoleException;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
public interface RoleService extends IService<Role> {
    
    /**
     * 添加角色
     * @param role 角色信息
     * @return 是否添加成功
     * @throws RoleException 无权限异常
     */
    boolean putRole(Role role) throws RoleException;
    
    /**
     * 删除角色
     * @param id 角色id
     * @return 是否删除成功
     * @throws RoleException 无权限异常
     */
    boolean deleteRole(String id) throws RoleException;
    
    /**
     * 修改角色
     * @param role 角色信息
     * @return 是否修改成功
     * @throws RoleException 无权限异常
     */
    boolean updateRole(Role role) throws RoleException;
    
    /**
     * 查询角色信息
     * @return 角色页
     * @throws RoleException 无权限异常
     */
    Page<Role> listRole() throws RoleException;
}
