package com.zhangjun.classdesign.slims.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;

import java.math.RoundingMode;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author 张钧
 * @since 2022-05-27
 */
@TableName(value = "`leave`")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Leave implements Serializable {

    private static final long serialVersionUID=1L;

    @TableField(exist = false)
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
    private Long studentId;

    @TableField(exist = false)
    @Setter
    @Getter
    private String studentName;

    /**
     * 辅导员编号
     */
    @Setter
    @Getter
    private Long instructorId;

    @TableField(exist = false)
    @Setter
    @Getter
    private String instructorName;

    /**
     * 请假类型  0：事假，1：病假
     */
    @Setter
    @Getter
    private Integer type;

    @TableField(exist = false)
    @Setter
    @Getter
    private String typeS;

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
    private Integer status;

    @TableField(exist = false)
    @Setter
    @Getter
    private String statusS;

    /**
     * 起始时间
     */
    @Setter
    @Getter
    private String startTime;

    /**
     * 结束时间
     */
    @Setter
    @Getter
    private String endTime;

    /**
     * 请假时长
     */
    private BigDecimal days;

    public BigDecimal getDays() {
        return days;
    }

    public Leave setDays() {
        if(startTime != null&&endTime!=null){
            LocalDateTime startTime = LocalDateTime.parse(this.startTime, formatter);
            LocalDateTime endTime = LocalDateTime.parse(this.endTime, formatter);
            Duration duration = Duration.between(startTime,endTime);
            this.days =  new BigDecimal("" +duration.getSeconds()).divide(new BigDecimal( ""+86400),3, RoundingMode.HALF_UP);
        }
        return this;
    }

    /**
     * 申请时间
     */
    @Setter
    @Getter
    private String createTime;

    @Override
    public String toString() {
        return "Leave{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", instructorId=" + instructorId +
                ", instructorName='" + instructorName + '\'' +
                ", type=" + type +
                ", typeS='" + typeS + '\'' +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                ", statusS='" + statusS + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", days=" + days +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
