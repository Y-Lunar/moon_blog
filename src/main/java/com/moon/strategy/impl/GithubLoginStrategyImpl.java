package com.moon.strategy.impl;

import com.moon.config.properties.GithubProperties;
import com.moon.entity.dto.CodeDto;
import com.moon.entity.vo.GitUserInfoVo;
import com.moon.entity.vo.SocialTokenVo;
import com.moon.entity.vo.SocialUserInfoVO;
import com.moon.entity.vo.TokenVO;
import com.moon.exception.ServiceException;
import com.moon.strategy.SocialLoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.moon.constant.SocialLoginConstant.*;
import static com.moon.enums.LoginTypeEnum.GITHUB;

/**
 * Github第三方登录策略
 *
 * @author:Y.0
 * @date:2023/10/24
 */

@Service("githubLoginStrategyImpl")
public class GithubLoginStrategyImpl extends AbstractLoginStrategyImpl {


    @Autowired(required = false)
    private GithubProperties githubProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public SocialTokenVo getSocialToken(CodeDto codeDTO) {
        // 获取Github的Token
        TokenVO githubToken = getGithubToken(codeDTO.getCode());
        // 返回Github的Token信息
        return SocialTokenVo.builder()
                .accessToken(githubToken.getAccess_token())
                .loginType(GITHUB.getLoginType())
                .build();
    }

    @Override
    public SocialUserInfoVO getSocialUserInfo(SocialTokenVo socialToken) {
        // 请求参数
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + socialToken.getAccessToken());
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(null, headers);
        // Gitee用户信息
        GitUserInfoVo gitUserInfoVO = restTemplate.exchange(githubProperties.getUserInfoUrl(),
                HttpMethod.GET,
                requestEntity,
                GitUserInfoVo.class).getBody();
        // 返回用户信息
        return SocialUserInfoVO.builder()
                .avatar(Objects.requireNonNull(gitUserInfoVO).getAvatar_url())
                .id(gitUserInfoVO.getId())
                .nickname(gitUserInfoVO.getLogin()).build();
    }

    /**
     * 获取Github的Token
     *
     * @param code 第三方code
     * @return {@link TokenVO} Github的Token
     */
    private TokenVO getGithubToken(String code) {
        // 根据code换取accessToken
        MultiValueMap<String, String> githubData = new LinkedMultiValueMap<>();
        // Github的Token请求参数
        githubData.add(CLIENT_ID, githubProperties.getClientId());
        githubData.add(CLIENT_SECRET, githubProperties.getClientSecret());
        githubData.add(REDIRECT_URI, githubProperties.getRedirectUrl());
        githubData.add(CODE, code);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept((List<MediaType>) MediaType.APPLICATION_JSON);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(githubData, headers);
        try {
            return restTemplate.exchange(githubProperties.getAccessTokenUrl(),
                    HttpMethod.POST,
                    requestEntity,
                    TokenVO.class).getBody();
        } catch (Exception e) {
            throw new ServiceException("Github登录错误");
        }
    }
}
