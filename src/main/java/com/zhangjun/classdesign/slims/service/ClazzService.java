package com.zhangjun.classdesign.slims.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjun.classdesign.slims.entity.Clazz;

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
}
