package com.zhangjun.classdesign.slims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Menu;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.mapper.MenuMapper;
import com.zhangjun.classdesign.slims.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.classdesign.slims.util.Result;
import com.zhangjun.classdesign.slims.util.RoleCheck;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     * 分页查询菜单
     *
     * @param aimPage  目标页
     * @param pageSize 页数
     * @return 菜单页
     * @exception RoleException 无权限
     */
    @Override
    public Page<Menu> listMenu(Integer aimPage, Integer pageSize) throws RoleException {
        if(RoleCheck.isAdmin()){
            Page<Menu> menuPage = new Page<>();
            menuPage.setSize(pageSize);
            menuPage.setCurrent(aimPage);
            menuPage.setRecords(this.list(new QueryWrapper<Menu>().last("limit " + (aimPage - 1) * pageSize + "," + pageSize)));
            long count = this.count();
            menuPage.setTotal(count%pageSize==0?count/pageSize:count/pageSize+1);
            return menuPage;
        }
        throw new RoleException("无权限访问");
    }
}
