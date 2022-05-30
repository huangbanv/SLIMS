package com.zhangjun.classdesign.slims.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.classdesign.slims.entity.Clazz;
import com.zhangjun.classdesign.slims.entity.User;
import com.zhangjun.classdesign.slims.entity.UserClazzGroup;
import com.zhangjun.classdesign.slims.enums.RoleEnum;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;
import com.zhangjun.classdesign.slims.mapper.ClazzMapper;
import com.zhangjun.classdesign.slims.service.ClazzService;
import com.zhangjun.classdesign.slims.service.UserClazzGroupService;
import com.zhangjun.classdesign.slims.util.Result;
import com.zhangjun.classdesign.slims.util.RoleCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 班级信息 服务实现类
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@Service
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {

    @Autowired
    UserClazzGroupService userClazzGroupService;

    /**
     * 获取辅导员Id
     *
     * @param id 学生id
     * @return 辅导员Id
     */
    @Override
    public Long getInstructorId(Long id) {
        UserClazzGroup userClazzGroup = userClazzGroupService.getOne(new QueryWrapper<UserClazzGroup>().eq("student_user_id", id));
        Clazz clazz = this.getOne(new QueryWrapper<Clazz>().eq("id", userClazzGroup.getClazzId()));
        return clazz.getInstructorUserId();
    }
    
    /**
     * 添加班级信息
     * @param clazz 班级信息
     * @return 是否添加成功
     * @throws RoleException 没有权限异常
     */
    @Override
    public boolean putClazz(Clazz clazz) throws RoleException {
        if(RoleCheck.isAdmin()){
            return save(clazz);
        }else {
            if(RoleCheck.getRoleCode().equals(RoleEnum.COLLEGE_ADMIN.getCode())){
                if(clazz.getDepartmentId().equals(RoleCheck.getDepartmentId())){
                   return save(clazz);
                }else {
                    throw new RoleException("您与班级的部门不同");
                }
            }else {
                throw new RoleException("您没有权限");
            }
        }
    }
    
    /**
     * 通过Id删除班级信息
     * @param id 班级Id
     * @return 是否删除成功
     * @throws RoleException 没有权限异常
     */
    @Override
    public boolean deleteClazz(Long id) throws RoleException {
        if(RoleCheck.isAdmin()){
            return removeById(new Clazz().setId(id));
        }else {
            if(RoleCheck.getRoleCode().equals(RoleEnum.COLLEGE_ADMIN.getCode())){
                Clazz old = getOne(new QueryWrapper<Clazz>().eq("id",id));
                if(RoleCheck.getDepartmentId().equals(old.getDepartmentId())){
                    return removeById(new Clazz().setId(id));
                }else {
                    throw new RoleException("您与班级的部门不同");
                }
            }else {
                throw new RoleException("您没有权限");
            }
        }
    }
    
    /**
     * 分页查询班级信息
     * @param aimPage 目标页面
     * @param pageSize 页面大小
     * @return 分页查找数据
     */
    @Override
    public Page<Clazz> listClazz(Integer aimPage, Integer pageSize) {
        Page<Clazz> clazzPage = new Page<>();
        clazzPage.setSize(pageSize);
        clazzPage.setCurrent(aimPage);
        return page(clazzPage);
    }
    
    /**
     * 修改班级信息
     * @param clazz 班级信息
     * @return 是否修改成功
     * @throws RoleException 没有权限异常
     */
    @Override
    public boolean updateClazz(Clazz clazz) throws RoleException {
        if (RoleCheck.isAdmin()) {
            return updateById(clazz);
        } else {
            if (RoleCheck.getRoleCode().equals(RoleEnum.COLLEGE_ADMIN.getCode())) {
                Clazz old = getOne(new QueryWrapper<Clazz>().eq("id", clazz.getId()));
                if (RoleCheck.getDepartmentId().equals(old.getDepartmentId())) {
                    return updateById(clazz);
                }else {
                    throw new RoleException("您与班级的部门不同");
                }
            }else {
                throw new RoleException("您没有权限");
            }
        }
    }
}
