package com.moon.config.satoken;

import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moon.entity.User;
import com.moon.entity.vo.OnlineUserVo;
import com.moon.mapper.UserMapper;
import com.moon.utils.IpUtils;
import com.moon.utils.UserAgentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import static com.moon.constant.CommonConstant.ONLINE_USER;
import static com.moon.enums.ZoneEnum.SHANGHAI;

/**
 * 自定义侦听器的实现
 *
 * @author:Y.0
 * @date:2023/10/9
 */


//框架默认内置了侦听器 SaTokenListenerForLog 实现 ，功能是控制台 log 打印输出，你可以通过配置sa-token.is-log=true开启。
@Component
public class MySaTokenListener implements SaTokenListener {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private HttpServletRequest request;

    /** 每次登录时触发 */
    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {
        System.out.println("---------- 自定义侦听器实现 doLogin");
        //查询出用户昵称
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getAvatar, User::getNickname)
                .eq(User::getId, loginId));
        //解析browser和os
        Map<String, String> userMap = UserAgentUtils.parseOsAndBrowser(request.getHeader("User-Agent"));
        // 获取登录ip地址
        String ipAddress = IpUtils.getIpAddress(request);
        // 获取登录地址
        String ipSource = IpUtils.getIpSource(ipAddress);
        // 获取登录时间
        LocalDateTime loginTime = LocalDateTime.now(ZoneId.of(SHANGHAI.getZone()));
        //获取出在线用户的用户信息
        OnlineUserVo onlineUserVo = OnlineUserVo.builder()
                .token(tokenValue)
                .id((Integer) loginId)
                .avatar(user.getAvatar())
                .nickname(user.getNickname())
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .os(userMap.get("os"))
                .browser(userMap.get("browser"))
                .loginTime(loginTime)
        .build();
//        接下来可以进行更新登录用户信息
        User newUser = User.builder()
                .id((Integer) loginId)
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .loginTime(loginTime)
                .build();
        userMapper.updateById(newUser);
        //用户在线信息存入tokenSession中
        SaSession session = StpUtil.getTokenSessionByToken(tokenValue);
        session.set(ONLINE_USER,onlineUserVo);
    }

    /** 每次注销时触发 */
    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        System.out.println("---------- 自定义侦听器实现 doLogout");
        // 删除缓存中的用户信息
        StpUtil.logoutByTokenValue(tokenValue);
    }

    /** 每次被踢下线时触发 */
    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {
        System.out.println("---------- 自定义侦听器实现 doKickout");
    }

    /** 每次被顶下线时触发 */
    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {
        System.out.println("---------- 自定义侦听器实现 doReplaced");
    }

    /** 每次被封禁时触发 */
    @Override
    public void doDisable(String loginType, Object loginId, String service, int level, long disableTime) {
        System.out.println("---------- 自定义侦听器实现 doDisable");
    }

    /** 每次被解封时触发 */
    @Override
    public void doUntieDisable(String loginType, Object loginId, String service) {
        System.out.println("---------- 自定义侦听器实现 doUntieDisable");
    }

    /** 每次二级认证时触发 */
    @Override
    public void doOpenSafe(String loginType, String tokenValue, String service, long safeTime) {
        System.out.println("---------- 自定义侦听器实现 doOpenSafe");
    }

    /** 每次退出二级认证时触发 */
    @Override
    public void doCloseSafe(String loginType, String tokenValue, String service) {
        System.out.println("---------- 自定义侦听器实现 doCloseSafe");
    }

    /** 每次创建Session时触发 */
    @Override
    public void doCreateSession(String id) {
        System.out.println("---------- 自定义侦听器实现 doCreateSession");
    }

    /** 每次注销Session时触发 */
    @Override
    public void doLogoutSession(String id) {
        System.out.println("---------- 自定义侦听器实现 doLogoutSession");
    }

    /** 每次Token续期时触发 */
    @Override
    public void doRenewTimeout(String tokenValue, Object loginId, long timeout) {
        System.out.println("---------- 自定义侦听器实现 doRenewTimeout");
    }
}
