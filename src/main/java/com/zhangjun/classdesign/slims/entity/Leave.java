package com.zhangjun.classdesign.slims.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 请假条
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Leave implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 单号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 学生编号
     */
    private Long studentUserId;

    /**
     * 辅导员编号
     */
    private Long instructorUserId;

    /**
     * 请假类型  0：事假，1：病假
     */
    private String type;

    /**
     * 请假原因
     */
    private String reason;

    /**
     * 起始时间
     */
    private Timestamp startTime;

    /**
     * 借书时间
     */
    private Timestamp endTime;

    /**
     * 请假时长
     */
    private BigDecimal days;

    /**
     * 申请时间
     */
    private Timestamp createTime;


}
