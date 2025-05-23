package com.moon.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 其他信息
 *
 * @author:Y.0
 * @date:2023/10/15
 */

@Data
@Builder
@ApiModel(description = "其他信息")
public class MetaVo {
    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String title;

    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "菜单图标")
    private String icon;

    /**
     * 是否隐藏
     */
    @ApiModelProperty(value = "是否隐藏")
    private Boolean hidden;

}
