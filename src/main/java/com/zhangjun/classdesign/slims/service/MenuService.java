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
     * @exception RoleException 无权限
     */
    Page<Menu> listMenu(Integer aimPage, Integer pageSize) throws RoleException;
}
