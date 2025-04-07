package com.moon.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.moon.annotation.AccessLimit;
import com.moon.entity.dto.CodeDto;
import com.moon.entity.dto.LoginDto;
import com.moon.entity.dto.MailRegister;
import com.moon.service.LoginService;
import com.moon.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.AddressException;
import java.io.UnsupportedEncodingException;

/**
 * 登录管理模块
 *
 * @author:Y.0
 * @date:2023/9/21
 */
@RestController
@Api(tags = "登录模块")
public class LoginController {

    @Autowired(required = false)
    private LoginService loginService;

    /**
     * 用户登录
     *
     * @param loginDto 登录参数
     * @return 返回值String
     */
    @ApiOperation(value = "用户登录接口")
    @PostMapping("/login")
    public Result<String> UserLogin(@Validated @RequestBody LoginDto loginDto){
        return Result.success(loginService.login(loginDto));
    }

    /**
     * 用户退出
     */
    @SaCheckLogin
    @ApiOperation(value = "用户退出")
    @GetMapping("/logout")
    public Result<?> logout() {
        StpUtil.logout();
        return Result.success();
    }

    /**
     * 发送邮箱验证码
     *
     * @param username 邮箱号
     * @return
     */
    @AccessLimit(seconds = 60, maxCount = 1)
    @ApiOperation(value = "发送邮箱验证码")
    @GetMapping("/mailCode")
    public Result<?> sendMailCode(String username) throws AddressException, UnsupportedEncodingException {
        loginService.sendMailCode(username);
        return Result.success();
    }

    /**
     * 用户邮箱注册账号
     *
     * @param mailRegister
     * @return
     */
    @ApiOperation(value = "邮箱注册")
    @PostMapping("/mailRegister")
    public Result<?> register(@Validated @RequestBody MailRegister mailRegister){
        loginService.mailRegister(mailRegister);
        return Result.success();
    }

    /**
     * Gitee登录
     *
     * @param data 第三方code
     * @return Token
     */
    @ApiOperation(value = "Gitee登录")
    @PostMapping("/oauth/gitee")
    public Result<String> giteeLogin(@RequestBody CodeDto data) {
        return Result.success(loginService.giteeLogin(data));
    }

    /**
     * Github登录
     *
     * @param data 第三方code
     * @return Token
     */
    @ApiOperation(value = "Github登录")
    @PostMapping("/oauth/github")
    public Result<String> githubLogin(@RequestBody CodeDto data) {
        return Result.success(loginService.githubLogin(data));
    }


}
