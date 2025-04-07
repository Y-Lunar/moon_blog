package com.moon.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.moon.entity.User;
import com.moon.entity.dto.UpdateEmailDto;
import com.moon.entity.dto.UserInfoDto;
import com.moon.entity.vo.RouterVo;
import com.moon.entity.vo.UserAdminInfoVO;
import com.moon.entity.vo.UserPwdDto;
import com.moon.entity.vo.UserVo;
import com.moon.service.UserService;
import com.moon.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户管理模块
 *
 * @author:Y.0
 * @date:2023/9/22
 */

@Api(tags = "用户管理模块")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取登录用户信息
     *
     * @return 用户信息userVo
     */
    @SaCheckLogin
    @ApiOperation(value = "获取登录用户信息")
    @GetMapping("/user/getUserInfo")
    public Result<UserVo> getUsers(){
        UserVo userVo = userService.getUsers();
        return Result.success(userVo);
    }

    /**
     * 获取后台登录用户信息
     *
     * @return
     */
    @ApiOperation(value = "获取后台登录用户信息")
    @GetMapping("/admin/user/getUserInfo")
    public Result<UserAdminInfoVO> getUserInfo() {
        UserAdminInfoVO userAdminInfoVO = userService.getUserInfo();
        return Result.success(userAdminInfoVO);
    }

    /**
     * 获取登录用户菜单
     *
     * @return
     */
    @ApiOperation(value = "获取登录用户菜单")
    @GetMapping("/admin/user/getUserMenu")
    public Result<List<RouterVo>> getUserMenu() {
        List<RouterVo> routerVoList = userService.getUserMenu();
        return Result.success(routerVoList);
    }


    /**
     * 修改用户邮箱
     *
     * @param updateEmailDto 邮箱信息以及验证码
     * @return
     */
    @ApiOperation(value = "修改用户邮箱")
    @SaCheckPermission(value = "user:email:update")
    @PutMapping("/user/mail")
    public Result<?> updateUserMail(@Validated @RequestBody UpdateEmailDto updateEmailDto){
        userService.updateUserMail(updateEmailDto);
        return Result.success();
    }

    /**
     * 修改用户头像
     *
     * @param file 头像地址
     * @return
     */
    @ApiOperation(value = "修改用户头像")
    @SaCheckPermission(value = "user:avatar:update")
    @PostMapping("/user/avatar")
    public Result<String> updateUserAvatar(@RequestParam(value = "file") MultipartFile file){
        return Result.success(userService.updateUserAvatar(file));
    }

    /**
     * 修改用户信息
     *
     * @param userInfoDto 用户信息
     * @return
     */
    @ApiOperation(value = "修改用户信息")
    @PutMapping("/user/info")
    @SaCheckPermission(value = "user:info:update")
    public Result<User> updateUserInfo(@Validated @RequestBody UserInfoDto userInfoDto){
        userService.updateUserInfo(userInfoDto);
        return Result.success();
    }

    /**
     * 修改用户密码
     *
     * @param userPwdDto
     * @return
     */
    @ApiOperation(value = "修改用户密码")
    @PutMapping("/user/password")
    public Result<?> updatePassword(@Validated @RequestBody UserPwdDto userPwdDto){
        userService.updateUserPwd(userPwdDto);
        return Result.success();
    }

}
