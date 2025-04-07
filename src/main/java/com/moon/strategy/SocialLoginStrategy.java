package com.moon.strategy;

import com.moon.entity.dto.CodeDto;

/**
 * 第三方登录
 *
 * @author:Y.0
 * @date:2023/10/24
 */
public interface SocialLoginStrategy {

    /**
     * 登录
     *
     * @param data 第三方code
     * @return {@link String} Token
     */
    String login(CodeDto data);

}
