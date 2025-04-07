package com.moon.service;

import com.moon.entity.dto.CodeDto;
import com.moon.entity.dto.LoginDto;
import com.moon.entity.dto.MailRegister;

import javax.mail.internet.AddressException;
import java.io.UnsupportedEncodingException;

/**
 * 登录业务接口
 * @author:Y.0
 * @date:2023/9/21
 */
public interface LoginService {

    /**
     * 用户登录
     * @param loginDto 登录参数
     * @return
     */
    String login(LoginDto loginDto);

    /**
     * 发送邮箱验证码
     * @param username 邮箱号
     */
    void sendMailCode(String username) throws UnsupportedEncodingException, AddressException;

    /**
     * 用户邮箱注册账号
     * @param mailRegister
     */
    void mailRegister(MailRegister mailRegister);

    /**
     * Gitee登录
     * @param data 第三方code
     */
    String giteeLogin(CodeDto data);

    /**
     * Github登录
     * @param data 第三方code
     */
    String githubLogin(CodeDto data);
}
