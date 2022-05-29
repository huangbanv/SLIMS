package com.zhangjun.classdesign.slims.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.classdesign.slims.entity.Clazz;
import com.zhangjun.classdesign.slims.entity.UserClazzGroup;
import com.zhangjun.classdesign.slims.mapper.ClazzMapper;
import com.zhangjun.classdesign.slims.service.ClazzService;
import com.zhangjun.classdesign.slims.service.UserClazzGroupService;
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
}
