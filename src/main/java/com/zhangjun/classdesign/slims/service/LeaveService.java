package com.zhangjun.classdesign.slims.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Leave;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjun.classdesign.slims.exception.RoleException;

/**
 * <p>
 * 请假条 服务类
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
public interface LeaveService extends IService<Leave> {

    /**
     * 请假
     * @param leave 请假信息
     * @return 是否请假成功
     * @throws RoleException 无权限异常
     */
    boolean putLeave(Leave leave) throws RoleException;

    /**
     * 分页查询请假
     * @param aimPage 目标页
     * @param pageSize 页大小
     * @return 请假页
     */
    Page<Leave> listLeave(Integer aimPage, Integer pageSize);

    /**
     * 删除和取消（逻辑删除）请假条
     * @param id 请假条id
     * @param logicalDelete 是否逻辑删除 0：否 1：是
     * @return 是否删除成功
     * @throws RoleException 无权限异常
     */
    boolean deleteLeave(Integer id, Integer logicalDelete) throws RoleException;

    /**
     * 修改请假单信息
     * @param leave 请假单信息
     * @return 是否删除成功
     * @throws RoleException 无权限异常
     */
    boolean updateLeave(Leave leave) throws RoleException;
    
    /**
     * 更新请假单状态
     * @param id 请假单id
     * @param status 状态
     * @return 是否修改成功
     * @throws RoleException 无权限异常
     */
    boolean changeStatus(Integer id, Integer status) throws RoleException;
    
    /**
     * 根据班级和时间段查询请假情况
     * @param aimPage 目标页
     * @param pageSize 页大小
     * @param clazzId 班级Id
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 请假情况
     * @throws RoleException 无权限异常
     */
    Page<Leave> listLeaveByClazzAndTime(Integer aimPage, Integer pageSize, Integer clazzId, String startDate,String endDate) throws RoleException;
}
