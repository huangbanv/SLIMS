package com.zhangjun.classdesign.slims.service.impl;

import com.zhangjun.classdesign.slims.entity.Department;
import com.zhangjun.classdesign.slims.mapper.DepartmentMapper;
import com.zhangjun.classdesign.slims.service.DepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
