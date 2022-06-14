package com.zhangjun.classdesign.slims.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;
/**
 * @author 张钧
 * @Description
 * @create 2022-06-14 22:18
 */
@Data
@Accessors(chain = true)
public class UserExcel {
    /**
     * id
     */
    @Excel(name = "Id")
    private Long id;

    /**
     * 姓名
     */
    @Excel(name = "姓名")
    private String name;

    /**
     * 账户
     */
    @Excel(name = "账户")
    private String account;

    /**
     * 密码
     */
    @Excel(name = "密码")
    private String password;

    /**
     * 所属部门
     */
    @Excel(name = "所属部门id")
    private String departmentId;

    /**
     * 所属部门名
     */
    @Excel(name = "所属部门名")
    private String departmentName;

    /**
     * 手机号
     */
    @Excel(name = "手机号")
    private String phone;

    /**
     * 性别   0：男   1：女
     */
    @Excel(name = "性别",replace = {"男_0","女_1"})
    private Integer gender;

    /**
     * 状态  0：停用   1：正常
     */
    @Excel(name = "状态",replace = {"停用_0","正常_1"})
    private Integer status;
    /**
     * 角色Id
     */
    @Excel(name = "角色id")
    private String roleId;

}
