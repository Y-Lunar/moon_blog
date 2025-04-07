package com.moon.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 登录信息
 * @author:Y.0
 * @date:2023/9/21
 **/
@Data
@ApiModel(description = "登录信息")
public class LoginDto {

        /**
         * 用户名
         */
        @NotBlank(message = "用户名不能为空")
        @ApiModelProperty(value = "用户名")
        private String username;

        /**
         * 用户密码
         */
        @NotBlank(message = "密码不能为空")
        @Size(min = 6, message = "密码不能少于6位")
        @ApiModelProperty(value = "用户密码")
        private String password;

}
