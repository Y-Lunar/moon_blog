package com.moon.service;

import com.moon.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.entity.dto.*;
import com.moon.entity.vo.*;
import com.moon.utils.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_user】的数据库操作Service
* @createDate 2023-09-21 16:17:04
*/
public interface UserService extends IService<User> {

    /**
     * 修改用户邮箱
     * @param updateEmailDto 邮箱信息
     */
    void updateUserMail(UpdateEmailDto updateEmailDto);

    /**
     * 修改用户头像
     * @param file 用户头像地址
     * @return
     */
    String updateUserAvatar(MultipartFile file);

    /**
     * 更新用户信息
     *
     * @param userInfoDto 用户信息
     */
    void updateUserInfo(UserInfoDto userInfoDto);

    /**
     * 获取登录用户信息
     *
     * @return 用户信息userVo
     */
    UserVo getUsers();

    /**
     * 修改用户密码
     *
     * @param userPwdDto 用户信息
     */
    void updateUserPwd(UserPwdDto userPwdDto);

    /**
     * 获取后台登录用户信息
     *
     * @return
     */
    UserAdminInfoVO getUserInfo();

    /**
     * 获取登录用户菜单
     *
     * @return
     */
    List<RouterVo> getUserMenu();

    /**
     * 获取后台登录用户信息
     *
     * @return 后台用户信息
     */
    UserBackInfoVo getUserBackInfo();

    /**
     * 查看后台用户列表
     *
     * @param condition 条件
     * @return 用户后台列表
     */
    PageResult<UserBackVO> listUserBackVO(ConditionDto condition);

    /**
     * 查看用户角色选项
     *
     * @return 用户角色选项
     */
    List<UserRoleVo> listUserRoleDTO();

    /**
     * 修改用户
     *
     * @param user 用户信息
     * @return {@link Result <>}
     */
    void updateUser(UserRoleDto user);

    /**
     * 修改用户状态
     *
     * @param disable 禁用信息
     * @return {@link Result<>}
     */
    void updateUserStatus(DisableDto disable);

    /**
     * 查看在线用户
     *
     * @param condition 条件
     * @return 在线用户列表
     */
    PageResult<OnlineVO> listOnlineUser(ConditionDto condition);

    /**
     * 下线用户
     *
     * @param token 在线token
     * @return
     */
    void kickOutUser(String token);

    /**
     * 修改管理员密码
     *
     * @param password 密码
     * @return {@link Result<>}
     */
    void updateAdminPassword(PasswordDto password);
}
