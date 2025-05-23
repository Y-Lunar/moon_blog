package com.moon.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户信息
 *
 * @author:Y.0
 * @date:2023/9/22
 */

@Data
@ApiModel(description = "用户信息")
public class UserInfoDto implements Serializable {

    /**
     * 用户昵称
     */
    @NotBlank(message = "昵称不能为空")
    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    /**
     * 个人网站
     */
    @ApiModelProperty(value = "个人网站")
    private String webSite;

    /**
     * 个人简介
     */
    @ApiModelProperty(value = "个人简历")
    private String intro;
}
