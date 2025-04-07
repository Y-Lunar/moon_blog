package com.moon.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 访问令牌Token
 *
 * @author:Y.0
 * @date:2023/10/24
 */

@Data
@ApiModel(description = "访问令牌Token")
public class TokenVO {
    /**
     * 访问令牌
     */
    @ApiModelProperty(value = "访问令牌")
    private String access_token;
}
