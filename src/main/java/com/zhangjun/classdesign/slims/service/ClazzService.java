package com.zhangjun.classdesign.slims.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjun.classdesign.slims.entity.Clazz;
import com.zhangjun.classdesign.slims.exception.RoleException;

import java.util.List;

/**
 * <p>
 * 班级信息 服务类
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
public interface ClazzService extends IService<Clazz> {

    /**
     * 获取辅导员Id
     * @param id 学生id
     * @return 辅导员Id
     */
    Long getInstructorId(Long id);
    
    /**
     * 添加班级信息
     * @param clazz 班级信息
     * @return 是否添加成功
     * @throws RoleException 没有权限异常
     */
    boolean putClazz(Clazz clazz) throws RoleException;
    
    /**
     * 通过Id删除班级信息
     * @param id 班级Id
     * @return 是否删除成功
     * @throws RoleException 没有权限异常
     */
    boolean deleteClazz(Long id) throws RoleException;
    
    /**
     * 分页查询班级信息
     * @param aimPage 目标页面
     * @param pageSize 页面大小
     * @return 分页查找数据
     * @throws RoleException 无权限异常
     */
    Page<Clazz> listClazz(Integer aimPage, Integer pageSize) throws RoleException;

    /**
     * 通过部门id查询班级信息
     * @param id 部门id
     * @return 分页查找数据
     * @throws RoleException 无权限异常
     */
    List<Clazz> listClazz(String id) throws RoleException;
    
    /**
     * 修改班级信息
     * @param clazz 班级信息
     * @return 是否修改成功
     * @throws RoleException 没有权限异常
     */
    boolean updateClazz(Clazz clazz) throws RoleException;
}
