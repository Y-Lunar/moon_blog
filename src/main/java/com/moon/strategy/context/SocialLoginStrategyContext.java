package com.moon.strategy.context;

import com.moon.entity.dto.CodeDto;
import com.moon.enums.LoginTypeEnum;
import com.moon.strategy.SocialLoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 第三方登录上下文策略
 *
 * @author:Y.0
 * @date:2023/10/24
 */

@Service
public class SocialLoginStrategyContext {

    @Autowired(required = false)
    private Map<String , SocialLoginStrategy> socialLoginStrategyMap;

    /**
     * 登录
     *
     * @param data          data
     * @param loginTypeEnum 登录枚举
     * @return {@link String} Token
     */
    public String executeLoginStrategy(CodeDto data, LoginTypeEnum loginTypeEnum) {
        return socialLoginStrategyMap.get(loginTypeEnum.getStrategy()).login(data);
    }
}
