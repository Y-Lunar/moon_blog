package com.moon.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 友链Dto
 *
 * @author:Y.0
 * @date:2023/10/23
 */

@Data
@ApiModel(description = "友链Dto")
public class FriendDto {

    /**
     * 友链id
     */
    @ApiModelProperty(value = "友链id")
    private Integer id;

    /**
     * 友链颜色
     */
    @NotBlank(message = "颜色不能为空")
    @ApiModelProperty(value = "友链颜色")
    private String color;

    /**
     * 友链名称
     */
    @NotBlank(message = "链接名不能为空")
    @ApiModelProperty(value = "友链名称")
    private String name;

    /**
     * 友链头像
     */
    @NotBlank(message = "头像不能为空")
    @ApiModelProperty(value = "友链头像")
    private String avatar;

    /**
     * 友链地址
     */
    @NotBlank(message = "地址不能为空")
    @ApiModelProperty(value = "友链地址")
    private String url;

    /**
     * 友链介绍
     */
    @NotBlank(message = "介绍不能为空")
    @ApiModelProperty(value = "友链介绍")
    private String introduction;

}
