package com.zhangjun.classdesign.slims.excel;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 张钧
 * @Description
 * @create 2022-06-06 13:49
 */
@Data
public class LeaveExcel {

    @ExcelProperty(value = {"Id"},index = 0)
    private Long id;

    @ExcelProperty(value = {"学生Id"},index = 1)
    private Long studentId;

    @ExcelProperty(value = {"学生姓名"},index = 2)
    private String studentName;

    @ExcelProperty(value = {"辅导员Id"},index = 3)
    private Long instructorId;

    @ExcelProperty(value = {"辅导员姓名"},index = 4)
    private String instructorName;

    @ExcelProperty(value = {"类型编号"},index = 5)
    private Integer type;

    @ExcelProperty(value = {"类型"},index = 6)
    private String typeS;

    @ExcelProperty(value = {"请假原因"},index = 7)
    private String reason;

    @ExcelProperty(value = {"状态编号"},index = 8)
    private Integer status;

    @ExcelProperty(value = {"状态"},index = 9)
    private String statusS;

    @ExcelProperty(value = {"开始时间"},index = 10)
    private String startTime;

    @ExcelProperty(value = {"结束时间"},index = 11)
    private String endTime;

    @ExcelProperty(value = {"请假时长"},index = 12)
    private BigDecimal days;

    @ExcelProperty(value = {"申请时间"},index = 13)
    private String createTime;
}
