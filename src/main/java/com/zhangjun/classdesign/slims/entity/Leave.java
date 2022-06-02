package com.zhangjun.classdesign.slims.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author 张钧
 * @since 2022-05-27
 */
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Leave implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 单号
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Setter
    @Getter
    private Long id;

    /**
     * 学生编号
     */
    @Setter
    @Getter
    private Long studentUserId;

    /**
     * 辅导员编号
     */
    @Setter
    @Getter
    private Long instructorUserId;

    /**
     * 请假类型  0：事假，1：病假
     */
    @Setter
    @Getter
    private String type;

    /**
     * 请假原因
     */
    @Setter
    @Getter
    private String reason;

    /**
     * 状态 0：未批准 1：已批准 2：已拒绝 3：已取消 4：已销假
     */
    @Setter
    @Getter
    private String status;

    /**
     * 起始时间
     */
    @Setter
    @Getter
    private Timestamp startTime;

    /**
     * 结束时间
     */
    @Setter
    @Getter
    private Timestamp endTime;

    /**
     * 请假时长
     */
    private BigDecimal days;

    public BigDecimal getDays() {
        return new BigDecimal((endTime.getTime()-startTime.getTime())/new Timestamp(86400000).getTime());
    }

    public void setDays() {
        if(startTime != null&&endTime!=null){
            this.days =  new BigDecimal((endTime.getTime()-startTime.getTime())/new Timestamp(86400000).getTime());
        }
    }

    /**
     * 申请时间
     */
    @Setter
    @Getter
    private Timestamp createTime;


}
