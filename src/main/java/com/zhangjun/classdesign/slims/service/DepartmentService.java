package com.zhangjun.classdesign.slims.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Department;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjun.classdesign.slims.exception.RoleException;

/**
 * <p>
 * 部门 服务类
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
public interface DepartmentService extends IService<Department> {
    
    /**
     * 添加部门信息
     * @param department 部门信息
     * @return 是否添加成功
     * @throws RoleException 权限异常
     */
    boolean putDepartment(Department department) throws RoleException;
    
    /**
     * 根据部门id删除部门
     * @param id 部门id
     * @return 是否删除成功
     * @throws RoleException 权限异常
     */
    boolean deleteDepartment(String id) throws RoleException;
    
    /**
     * 分页查询部门信息
     * @param aimPage 目标页
     * @param pageSize 页面大小
     * @return 部门信息
     */
    Page<Department> listDepartment(Integer aimPage, Integer pageSize);
    
    /**
     * 更新部门信息
     * @param department 部门信息
     * @return 是否更新成功
     * @throws RoleException 权限异常
     */
    boolean updateDepartment(Department department) throws RoleException;
}
