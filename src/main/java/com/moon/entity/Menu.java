package com.moon.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_menu
 */
@TableName(value ="t_menu")
@Data
public class Menu implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 父菜单id (paren_id为0且type为M则是一级菜单)
     */
    private Integer parentId;

    /**
     * 权限类型 (M目录 C菜单 B按钮)
     */
    private String menuType;

    /**
     * 名称
     */
    private String menuName;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 菜单组件
     */
    private String component;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 是否隐藏 (0否 1是)
     */
    private Integer isHidden;

    /**
     * 是否禁用 (0否 1是)
     */
    private Integer isDisable;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}