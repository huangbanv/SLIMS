package com.zhangjun.classdesign.slims.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 菜单
 * </p>
 *
 * @author 张钧
 * @since 2022-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Menu implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 菜单代码
     */
    private String menuCode;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Timestamp createDate;

    /**
     * 更新时间
     */
    private Timestamp updateDate;

    @TableField(exist = false)
    private Integer permission;
}
