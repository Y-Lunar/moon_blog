package com.moon.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户菜单
 *
 * @author:Y.0
 * @date:2023/10/15
 */

@Data
@ApiModel(description = "用户菜单")
public class UserMenuVo {

    /**
     * 菜单id
     */
    @ApiModelProperty(value = "菜单id")
    private Integer id;

    /**
     * 父菜单id
     */
    @ApiModelProperty(value = "父菜单id")
    private Integer parentId;

    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    /**
     * 类型（M目录 C菜单 B按钮）
     */
    @ApiModelProperty(value = "类型（M目录 C菜单 B按钮）")
    private String menuType;

    /**
     * 路由地址
     */
    @ApiModelProperty(value = "路由地址")
    private String path;

    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "菜单图标")
    private String icon;

    /**
     * 菜单组件
     */
    @ApiModelProperty(value = "菜单组件")
    private String component;

    /**
     * 是否隐藏 (0否 1是)
     */
    @ApiModelProperty(value = "是否隐藏 (0否 1是)")
    private Integer isHidden;

}
