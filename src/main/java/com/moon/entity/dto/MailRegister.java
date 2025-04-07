package com.moon.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户邮箱注册账号信息
 * @author:Y.0
 * @date:2023/9/21
 */
@Data
@ApiModel(description = "用户注册信息")
public class MailRegister {

        /**
         * 用户名
         */
        @NotBlank(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        @ApiModelProperty(value = "用户名")
        private String username;

        /**
         * 密码
         */
        @NotBlank(message = "密码不能为空")
        @Size(min = 6, message = "密码不能少于6位")
        @ApiModelProperty(value = "密码")
        private String password;

        /**
         * 验证码
         */
        @NotBlank(message = "验证码不能为空")
        @ApiModelProperty(value = "验证码")
        private String code;
}
