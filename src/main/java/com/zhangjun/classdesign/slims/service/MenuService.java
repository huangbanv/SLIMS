package com.zhangjun.classdesign.slims.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjun.classdesign.slims.exception.RoleException;

/**
 * <p>
 * 菜单 服务类
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
public interface MenuService extends IService<Menu> {

    /**
     * 分页查询菜单
     * @param aimPage 目标页
     * @param pageSize 页数
     * @return 菜单页
     * @throws  RoleException 无权限
     */
    Page<Menu> listMenu(Integer aimPage, Integer pageSize) throws RoleException;
    
    /**
     * 创建菜单
     * @param menu 菜单
     * @return 是否创建成功
     * @throws RoleException 无权限
     */
    boolean putMenu(Menu menu) throws RoleException;
    
    /**
     * 更新菜单
     * @param menu 菜单
     * @return 是否更新成功
     * @throws RoleException 无权限
     */
    boolean updateMenu(Menu menu) throws RoleException;
    
    /**
     * 删除菜单
     * @param id 菜单Id
     * @return 是否删除成功
     * @throws RoleException 无权限
     */
    boolean deleteMenu(Long id) throws RoleException;
}
