package com.moon.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 第三方登录信息
 *
 * @author:Y.0
 * @date:2023/10/24
 */

@ApiModel(description = "第三方登录信息")
@Data
public class CodeDto {

    /**
     * code
     */
    @NotBlank(message = "code不能为空")
    @ApiModelProperty(value = "code",required = true)
    private String code;

}
