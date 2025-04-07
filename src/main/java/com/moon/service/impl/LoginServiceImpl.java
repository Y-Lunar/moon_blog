package com.moon.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.moon.entity.SiteConfig;
import com.moon.entity.User;
import com.moon.entity.UserRole;
import com.moon.entity.dto.CodeDto;
import com.moon.entity.dto.LoginDto;
import com.moon.entity.dto.MailDto;
import com.moon.entity.dto.MailRegister;
import com.moon.enums.LoginTypeEnum;
import com.moon.mapper.UserMapper;
import com.moon.mapper.UserRoleMapper;
import com.moon.service.LoginService;
import com.moon.service.RedisService;
import com.moon.strategy.context.SocialLoginStrategyContext;
import com.moon.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.moon.constant.CommonConstant.*;
import static com.moon.constant.RedisConstant.*;
import static com.moon.utils.CommonUtils.checkEmail;
import static com.moon.enums.LoginTypeEnum.*;
import static com.moon.enums.RoleEnum.USER;

/**
 * 登录业务接口实现层
 *
 * @author:Y.0
 * @date:2023/9/21
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    @Autowired(required = false)
    private UserRoleMapper userRoleMapper;

    @Autowired(required = false)
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private SocialLoginStrategyContext socialLoginStrategyContext;

    /**
     * 登录实现
     *
     * @param loginDto 登录参数
     * @return
     */
    @Override
    public String login(LoginDto loginDto) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getId)
                .eq(User::getUsername, loginDto.getUsername())
                //使用自己封装的Security工具类，然后来实现对密码的加密比较
                .eq(User::getPassword, SecurityUtils.Encrypt(loginDto.getPassword(),loginDto.getUsername())));
        Assert.notNull(user, "用户不存在或密码错误");
        // 校验指定账号是否已被封禁，如果被封禁则抛出异常 `DisableServiceException`
        StpUtil.checkDisable(user.getId());
        //通过sa-token校验才进行最后的登录
        StpUtil.login(user.getId());
        return StpUtil.getTokenValue();
    }

    /**
     * 发送邮箱验证码
     *
     * @param username 邮箱号
     */
    @Override
    public void sendMailCode(String username) {
        Assert.isTrue(checkEmail(username), "请输入正确的邮箱！");
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", 6);
        String code = randomGenerator.generate();
        //创建邮件正文
        Context context = new Context();
        context.setVariable("code", Arrays.asList(code.split("")));
        //将模块引擎内容解析成html字符串
        String emailContent = templateEngine.process("EmailVerificationCode", context);
        MimeMessage message = javaMailSender.createMimeMessage();
        // 对方看到的发送人
//        message.setFrom(new InternetAddress(MimeUtility.encodeText("￼")+"<y0962464@foxmail.com>").toString());
        //发送邮件
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            // 对方看到的发送人
            helper.setFrom(new InternetAddress(MimeUtility.encodeText("￼")+"<y0962464@foxmail.com>").toString());
            //收件人
            helper.setTo(username);
            //主题
            helper.setSubject("注册验证码");
            // 内容
            helper.setText(emailContent,true);
            javaMailSender.send(message);
            redisService.setObject(CODE_KEY + username, code, CODE_EXPIRE_TIME, TimeUnit.MINUTES);
            log.info("邮件发送成功");
        }catch (Exception e){
            log.error("邮件发送出现异常");
            log.error("异常信息为"+e.getMessage());
            log.error("异常堆栈信息为-->");
            e.printStackTrace();
        }
    }

    /**
     * 用户邮箱注册账号
     *
     * @param mailRegister 注册邮箱信息
     */
    @Override
    public void mailRegister(MailRegister mailRegister) {
        //校验客户端输入的验证码是否正确，使用hutool工具来实现
        String sysCode = redisService.getObject(CODE_KEY + mailRegister.getUsername());
        Assert.notBlank(sysCode, "验证码未发送或已过期!");
        Assert.isTrue(sysCode.equals(mailRegister.getCode()), "验证码错误，请重新输入!");
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getUsername)
                .eq(User::getUsername, mailRegister.getUsername()));
        Assert.isNull(user,"邮箱已注册!");
        SiteConfig siteConfig = redisService.getObject(SITE_SETTING);
        //新增用户
        User newUser = User.builder()
                .username(mailRegister.getUsername())
                .email(mailRegister.getUsername())
                .nickname(USER_NICKNAME + IdWorker.getId())
                .avatar(siteConfig.getUserAvatar())
                .password(SecurityUtils.Encrypt(mailRegister.getPassword(),mailRegister.getUsername()))
                .loginType(EMAIL.getLoginType())
                .isDisable(FALSE)
                .build();
        userMapper.insert(newUser);
        //为新增的用户添加角色
        UserRole userRole = UserRole.builder()
                .userId(newUser.getId())
                .roleId(USER.getRoleId())
                .build();
        userRoleMapper.insert(userRole);
    }

    /**
     * Gitee登录
     *
     * @param data 第三方code
     * @return Token
     */
    @Transactional(rollbackFor = Exception.class)
    public String giteeLogin(CodeDto data) {
        return socialLoginStrategyContext.executeLoginStrategy(data, LoginTypeEnum.GITEE);
    }

    /**
     * Github登录
     *
     * @param data 第三方code
     * @return Token
     */
    @Transactional(rollbackFor = Exception.class)
    public String githubLogin(CodeDto data) {
        return socialLoginStrategyContext.executeLoginStrategy(data, LoginTypeEnum.GITHUB);
    }
}
