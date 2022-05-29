package com.zhangjun.classdesign.slims.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户班级关系
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserClazzGroup implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 学生编号
     */
    private Long studentUserId;

    /**
     * 班级编号
     */
    private String clazzId;

    /**
     * 开始时间
     */
    private Timestamp startTime;

    /**
     * 结束时间
     */
    private Timestamp endTime;


}
