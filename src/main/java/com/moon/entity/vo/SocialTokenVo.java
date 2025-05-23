package com.moon.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 第三方token
 *
 * @author:Y.0
 * @date:2023/10/24
 */
@Data
@Builder
@ApiModel(description = "第三方token")
public class SocialTokenVo {
    /**
     * 开放id
     */
    @ApiModelProperty(value = "开放id")
    private String openId;

    /**
     * 访问令牌
     */
    @ApiModelProperty(value = "访问令牌")
    private String accessToken;

    /**
     * 登录类型
     */
    @ApiModelProperty(value = "登录类型")
    private Integer loginType;
}
