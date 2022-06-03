package com.zhangjun.classdesign.slims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Menu;
import com.zhangjun.classdesign.slims.enums.HttpStatus;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.mapper.MenuMapper;
import com.zhangjun.classdesign.slims.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.classdesign.slims.util.RoleCheck;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜单 服务实现类
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    
    /**
     * 更新菜单
     *
     * @param menu 菜单
     * @return 是否更新成功
     * @throws RoleException 无权限
     */
    @Override
    public boolean updateMenu(Menu menu) throws RoleException {
        if(RoleCheck.isAdmin()){
            if(menu.getId()!=null){
                return updateById(menu);
            }
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }

    /**
     * 展示所有菜单
     *
     * @return 菜单列表
     * @throws RoleException 无权限
     */
    @Override
    public List<Menu> listAll() throws RoleException {
        if(RoleCheck.isAdmin()){
            return list();
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }

    /**
     * 删除菜单
     *
     * @param id 菜单Id
     * @return 是否删除成功
     * @throws RoleException 无权限
     */
    @Override
    public boolean deleteMenu(Long id) throws RoleException {
        if(RoleCheck.isAdmin()){
            return removeById(id);
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }
    
    /**
     * 分页查询菜单
     *
     * @param aimPage  目标页
     * @param pageSize 页数
     * @return 菜单页
     * @throws RoleException 无权限
     */
    @Override
    public Page<Menu> listMenu(Integer aimPage, Integer pageSize) throws RoleException {
        if(RoleCheck.isAdmin()){
            Page<Menu> menuPage = new Page<>();
            menuPage.setSize(pageSize);
            menuPage.setCurrent(aimPage);
            menuPage.setRecords(this.list(new QueryWrapper<Menu>().last("limit " + (aimPage - 1) * pageSize + "," + pageSize)));
            menuPage.setTotal(this.count());
            return menuPage;
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }
    
    /**
     * 创建菜单
     * @param menu 菜单
     * @return 是否创建成功
     * @throws RoleException 无权限
     */
    @Override
    public boolean putMenu(Menu menu) throws RoleException {
        if(RoleCheck.isAdmin()){
            return save(menu);
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }
}
