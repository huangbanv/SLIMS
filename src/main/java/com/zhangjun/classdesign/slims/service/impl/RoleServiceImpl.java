package com.zhangjun.classdesign.slims.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Role;
import com.zhangjun.classdesign.slims.enums.HttpStatus;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.mapper.RoleMapper;
import com.zhangjun.classdesign.slims.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.classdesign.slims.util.Result;
import com.zhangjun.classdesign.slims.util.RoleCheck;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    
    /**
     * 添加角色
     *
     * @param role 角色信息
     * @return 是否添加成功
     * @throws RoleException 无权限异常
     */
    @Override
    public boolean putRole(Role role) throws RoleException {
        if(RoleCheck.isAdmin()){
            return save(role);
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }
    
    /**
     * 修改角色
     *
     * @param role 角色信息
     * @return 是否修改成功
     * @throws RoleException 无权限异常
     */
    @Override
    public boolean updateRole(Role role) throws RoleException {
        if(RoleCheck.isAdmin()){
            return updateById(role);
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }
    
    /**
     * 查询角色信息
     *
     * @return 角色页
     * @throws RoleException 无权限异常
     */
    @Override
    public Page<Role> listRole() throws RoleException {
        if(RoleCheck.isAdmin()){
            return page(new Page<>());
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }
    
    /**
     * 删除角色
     *
     * @param id 角色id
     * @return 是否删除成功
     * @throws RoleException 无权限异常
     */
    @Override
    public boolean deleteRole(String id) throws RoleException {
        if(RoleCheck.isAdmin()){
            return removeById(id);
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }
}
