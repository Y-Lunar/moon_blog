package com.moon.strategy.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moon.entity.User;
import com.moon.entity.UserRole;
import com.moon.entity.dto.CodeDto;
import com.moon.entity.vo.SocialTokenVo;
import com.moon.entity.vo.SocialUserInfoVO;
import com.moon.mapper.UserMapper;
import com.moon.mapper.UserRoleMapper;
import com.moon.strategy.SocialLoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.moon.enums.RoleEnum.USER;

/**
 * 第三方抽象登录模板
 *
 * @author:Y.0
 * @date:2023/10/24
 */

@Service
public abstract class AbstractLoginStrategyImpl implements SocialLoginStrategy {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private UserRoleMapper userRoleMapper;

    @Override
    public String login(CodeDto data) {
        User user;
        // 获取token
        SocialTokenVo socialToken = getSocialToken(data);
        // 获取用户信息
        SocialUserInfoVO socialUserInfoVO = getSocialUserInfo(socialToken);
        // 判断是否已注册
        User existUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getId)
                .eq(User::getUsername, socialUserInfoVO.getId())
                .eq(User::getLoginType, socialToken.getLoginType()));
        // 用户未登录过
        if (Objects.isNull(existUser)) {
            user = saveLoginUser(socialToken, socialUserInfoVO);
        } else {
            user = existUser;
        }
        // 校验指定账号是否已被封禁，如果被封禁则抛出异常 `DisableServiceException`
        StpUtil.checkDisable(user.getId());
        // 通过校验后，再进行登录
        StpUtil.login(user.getId());
        return StpUtil.getTokenValue();
    }

    /**
     * 获取第三方Token
     *
     * @param data data
     * @return 第三方token
     */
    public abstract SocialTokenVo getSocialToken(CodeDto data);

    /**
     * 获取第三方用户信息
     *
     * @param socialToken 第三方token
     * @return {@link SocialUserInfoVO} 第三方用户信息
     */
    public abstract SocialUserInfoVO getSocialUserInfo(SocialTokenVo socialToken);

    /**
     * 新增用户账号
     *
     * @param socialToken 第三方Token
     * @return {@link User} 登录用户身份权限
     */
    private User saveLoginUser(SocialTokenVo socialToken, SocialUserInfoVO socialUserInfoVO) {
        // 新增用户信息
        User newUser = User.builder()
                .avatar(socialUserInfoVO.getAvatar())
                .nickname(socialUserInfoVO.getNickname())
                .username(socialUserInfoVO.getId())
                .password(socialToken.getAccessToken())
                .loginType(socialToken.getLoginType())
                .build();
        userMapper.insert(newUser);
        // 新增用户角色
        UserRole userRole = UserRole.builder()
                .userId(newUser.getId())
                .roleId(USER.getRoleId())
                .build();
        userRoleMapper.insert(userRole);
        return newUser;
    }

}
