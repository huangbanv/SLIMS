package com.zhangjun.classdesign.slims.service.impl;

import com.zhangjun.classdesign.slims.entity.Department;
import com.zhangjun.classdesign.slims.enums.HttpStatus;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.mapper.DepartmentMapper;
import com.zhangjun.classdesign.slims.service.DepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.classdesign.slims.util.RoleCheck;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 部门 服务实现类
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
    
    /**
     * 根据部门id删除部门
     *
     * @param id 部门id
     * @return 是否删除成功
     * @throws RoleException 权限异常
     */
    @Override
    public boolean deleteDepartment(String id) throws RoleException {
        if (RoleCheck.isAdmin()) {
            return removeById(id);
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }
    
    /**
     * 添加部门信息
     *
     * @param department 部门信息
     * @return 是否添加成功
     * @throws RoleException 权限异常
     */
    @Override
    public boolean putDepartment(Department department) throws RoleException {
        if (RoleCheck.isAdmin()) {
            return save(department);
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }
    
    /**
     * 仅提供id与部门名
     * @return 部门列表
     */
    @Override
    public List<Department> listDepartment() {
        return list();
    }
    
    /**
     * 分页查询部门信息
     * @return 部门信息
     */
    @Override
    public List<Department> listDepartmentDetail() {
        List<Department> result = list();
        List<Department> delete = new ArrayList<>();
        for (Department record : result) {
            if(!"0".equals(record.getPid())){
                for (Department department : result) {
                    if(department.getId().equals(record.getPid())){
                        department.getChildren().add(record);
                    }
                }
                delete.add(record);
            }
        }
        for (Department department : delete) {
            result.remove(department);
        }
        return result;
    }
    
    /**
     * 更新部门信息
     *
     * @param department 部门信息
     * @return 是否更新成功
     * @throws RoleException 权限异常
     */
    @Override
    public boolean updateDepartment(Department department) throws RoleException {
        if (RoleCheck.isAdmin()) {
            return updateById(department);
        }
        throw new RoleException(HttpStatus.NO_PERMISSION.getMessage());
    }
}
