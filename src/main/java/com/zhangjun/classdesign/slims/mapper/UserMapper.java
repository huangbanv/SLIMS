package com.zhangjun.classdesign.slims.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
public interface UserMapper extends BaseMapper<User> {

}
