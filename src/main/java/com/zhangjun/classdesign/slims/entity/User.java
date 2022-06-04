package com.zhangjun.classdesign.slims.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author 张钧
 * @since 2022-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 账户
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 所属部门
     */
    private String departmentId;

    /**
     * 所属部门名
     */
    @TableField(exist = false)
    private String departmentName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别   0：男   1：女
     */
    private Integer gender;

    /**
     * 状态  0：停用   1：正常
     */
    private Integer status;

    @TableField(exist = false)
    private String genderS;

    @TableField(exist = false)
    private String statusS;

    /**
     * 创建时间
     */
    private String createDate;


    /**
     * 更新时间
     */
    @TableField
    private String updateDate;

    /**
     * 角色Id
     */
    @TableField(exist = false)
    private String roleId;

    @TableField(exist = false)
    private String roleName;
    /**
     * 可用的菜单栏
     */
    @TableField(exist = false)
    private List<Menu> menus;

    @TableField(exist = false)
    private Long clazzId;

    @TableField(exist = false)
    private String clazzName;

}
