package com.zhangjun.classdesign.slims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhangjun.classdesign.slims.entity.Menu;
import com.zhangjun.classdesign.slims.entity.RoleMenuGroup;
import com.zhangjun.classdesign.slims.enums.HttpStatus;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.mapper.MenuMapper;
import com.zhangjun.classdesign.slims.mapper.RoleMenuGroupMapper;
import com.zhangjun.classdesign.slims.service.RoleMenuGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.classdesign.slims.util.RoleCheck;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色菜单关系 服务实现类
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@Service
public class RoleMenuGroupServiceImpl extends ServiceImpl<RoleMenuGroupMapper, RoleMenuGroup> implements RoleMenuGroupService {

    @Resource
    MenuMapper menuMapper;

    /**
     * 添加角色可访问菜单
     *
     * @param roleMenuGroup 角色菜单关系
     * @return 是否添加成功
     * @throws RoleException 无权限
     */
    @Override
    public boolean putRoleMenuGroup(RoleMenuGroup roleMenuGroup) throws RoleException {
        if(RoleCheck.isAdmin()){
            RoleMenuGroup one = getOne(new QueryWrapper<RoleMenuGroup>()
                    .eq("role_id", roleMenuGroup.getRoleId()).eq("menu_id", roleMenuGroup.getMenuId()));
            if(one == null){
                return save(roleMenuGroup);
            }
            return false;
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }

    /**
     * 通过角色菜单id删除相应关系
     *
     * @param id 角色菜单id
     * @return 是否删除成功
     * @throws RoleException 无权限
     */
    @Override
    public boolean deleteRoleMenuById(Long id) throws RoleException {
        if(RoleCheck.isAdmin()){
            return removeById(id);
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }

    /**
     * 通过角色id查询可用菜单
     *
     * @param id 角色id
     * @return 可用菜单
     * @throws RoleException 无权限
     */
    @Override
    public List<Menu> listMenusByRoleId(String id) throws RoleException {
        if(RoleCheck.isAdmin()){
            Map<Long, Long> map = list(new QueryWrapper<RoleMenuGroup>().eq("role_id", id))
                    .stream().collect(Collectors.toMap(RoleMenuGroup::getMenuId, RoleMenuGroup::getId));
            if(map.size() >0){
                List<Menu> menus = menuMapper.selectList(new QueryWrapper<Menu>().in("id", map.keySet()));
                menus.forEach(menu -> menu.setMenuRoleId(map.get(menu.getId())));
                return menus;
            }
            return null;
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }
}
