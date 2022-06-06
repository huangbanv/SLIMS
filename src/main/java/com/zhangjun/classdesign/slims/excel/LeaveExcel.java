package com.zhangjun.classdesign.slims.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author 张钧
 * @Description
 * @create 2022-06-06 13:49
 */
@Data
@Accessors(chain = true)
public class LeaveExcel {

    @Excel(name = "Id",width = 5)
    private Long id;

    @Excel(name = "学生Id",width = 10)
    private Long studentId;

    @Excel(name = "学生姓名",width = 10)
    private String studentName;

    @Excel(name = "辅导员Id",width = 10)
    private Long instructorId;

    @Excel(name = "辅导员姓名",width = 15)
    private String instructorName;

    @Excel(name = "类型编号",width = 10)
    private Integer type;

    @Excel(name = "类型",width = 5)
    private String typeS;

    @Excel(name = "请假原因",width = 50)
    private String reason;

    @Excel(name = "状态编号",width = 10)
    private Integer status;

    @Excel(name = "状态",width = 10)
    private String statusS;

    @Excel(name = "开始时间",width = 20)
    private String startTime;

    @Excel(name = "结束时间",width = 20)
    private String endTime;

    @Excel(name = "请假时长",width = 10)
    private BigDecimal days;

    @Excel(name = "申请时间",width = 20)
    private String createTime;
}
