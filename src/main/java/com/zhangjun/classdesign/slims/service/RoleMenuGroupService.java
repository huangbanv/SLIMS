package com.zhangjun.classdesign.slims.service;

import com.zhangjun.classdesign.slims.entity.Menu;
import com.zhangjun.classdesign.slims.entity.RoleMenuGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjun.classdesign.slims.exception.RoleException;

import java.util.List;

/**
 * <p>
 * 角色菜单关系 服务类
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
public interface RoleMenuGroupService extends IService<RoleMenuGroup> {

    /**
     * 通过角色id查询可用菜单
     * @param id 角色id
     * @return 可用菜单
     * @throws RoleException 无权限
     */
    List<Menu> listMenusByRoleId(String id) throws RoleException;

    /**
     * 通过角色菜单id删除相应关系
     * @param id 角色菜单id
     * @return 是否删除成功
     * @throws RoleException 无权限
     */
    boolean deleteRoleMenuById(Long id) throws RoleException;

    /**
     * 添加角色可访问菜单
     * @param roleMenuGroup 角色菜单关系
     * @return 是否添加成功
     * @throws RoleException 无权限
     */
    boolean putRoleMenuGroup(RoleMenuGroup roleMenuGroup) throws RoleException;
}
