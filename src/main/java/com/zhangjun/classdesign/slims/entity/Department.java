package com.zhangjun.classdesign.slims.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.ArrayList;
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
public class Department implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 编号  部门类型 X：行政 J：教学 S：系统管理
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 上级编号
     */
    private String pid;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 更新时间
     */
    private String updateDate;
    
    @TableField(exist = false)
    private List<Department> children = new ArrayList<>();

}
