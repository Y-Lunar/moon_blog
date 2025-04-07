package com.moon.utils;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * 实现密码加密
 *
 * @author:Y.0
 * @date:2023/9/21
 */
public class SecurityUtils {

    /**
     * 进行密码加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    public static String Encrypt(String password,String UserName){
        String saltedPassword = password + UserName;
        return SaSecureUtil.sha256(saltedPassword);
    }

    /**
     * 进行密码的校验是否正确
     *
     * @param oldPassword 数据库中的加密后的密码
     * @param newPassword 客户端输入的明文密码
     * @return 两次密码是否一致
     */
    public static boolean checkPwd(String oldPassword,String newPassword,String UserName){
        String encryptedPassword = Encrypt(newPassword,UserName);
        return StringUtils.equals(encryptedPassword,oldPassword);
    }
}
