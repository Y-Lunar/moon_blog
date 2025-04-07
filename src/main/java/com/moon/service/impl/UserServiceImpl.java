package com.moon.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.entity.User;
import com.moon.entity.UserRole;
import com.moon.entity.dto.*;
import com.moon.entity.vo.*;
import com.moon.enums.FilePathEnum;
import com.moon.exception.ServiceException;
import com.moon.mapper.MenuMapper;
import com.moon.mapper.RoleMapper;
import com.moon.mapper.UserRoleMapper;
import com.moon.service.RedisService;
import com.moon.service.UserService;
import com.moon.mapper.UserMapper;
import com.moon.strategy.context.UploadStrategyContext;
import com.moon.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static com.moon.constant.CommonConstant.*;
import static com.moon.constant.RedisConstant.*;
import static com.moon.utils.PageUtils.getLimit;
import static com.moon.utils.PageUtils.getSize;

/**
* @author Y.0
* @description 针对表【t_user】的数据库操作Service实现
* @createDate 2023-09-21 16:17:04
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private RedisService redisService;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private UploadStrategyContext uploadStrategyContext;

    @Autowired(required = false)
    private UserRoleMapper userRoleMapper;

    @Autowired(required = false)
    private MenuMapper menuMapper;

    @Autowired(required = false)
    private RoleMapper roleMapper;

    /**
     * 修改用户邮箱
     *
     * @param updateEmailDto 邮箱信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserMail(UpdateEmailDto updateEmailDto) {
        verifyMailOrCode(updateEmailDto.getEmail(),updateEmailDto.getCode());
        User user = User.builder()
                .id(StpUtil.getLoginIdAsInt())
                .email(updateEmailDto.getEmail())
                .build();
        userMapper.updateById(user);
    }

    /**
     * 修改用户头像
     *
     * @param file 用户头像地址
     * @return
     */
    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public String updateUserAvatar(MultipartFile file) {
        //通过七牛云实现图片上传
//        String fileName = file.getOriginalFilename();
//        String filePath = AvatarPathUtils.generateFilePath(fileName);
//        String uploadedOss = ossUtils.uploadOss(file, FilePathEnum.AVATAR.getPath() + filePath);

//        Path path = Paths.get(fileName);
//        String filenameWithoutExtension = path.getFileName().toString();
//        int lastDotIndex = filenameWithoutExtension.lastIndexOf(".");
//        if (lastDotIndex > 0) {
//            filenameWithoutExtension = filenameWithoutExtension.substring(0, lastDotIndex);
//        }
//        System.out.println(filenameWithoutExtension); // 输出 "d4eac84067a547fa9c868970a36b63a6"

//         头像上传 -- 上传策略
        String avatar = uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.AVATAR.getPath());
        // 更新用户头像
        User newUser = User.builder()
                .id(StpUtil.getLoginIdAsInt())
                .avatar(avatar)
                .build();
        userMapper.updateById(newUser);
        log.info("文件路径：{},",avatar);
        return avatar;

    }

    /**
     * 修改用户信息
     *
     * @param userInfoDto 用户信息
     */
    @Override
    public void updateUserInfo(UserInfoDto userInfoDto) {
        User newUser = User.builder()
                .id(StpUtil.getLoginIdAsInt())
                .nickname(userInfoDto.getNickname())
                .intro(userInfoDto.getIntro())
                .webSite(userInfoDto.getWebSite())
                .build();
        userMapper.updateById(newUser);
    }


    /**
     * 获取登录用户信息
     *
     * @return 用户信息userVo
     */
    @Override
    public UserVo getUsers() {
        Integer userId = StpUtil.getLoginIdAsInt();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getNickname, User::getAvatar, User::getUsername, User::getWebSite,
                        User::getIntro, User::getEmail, User::getLoginType)
                .eq(User::getId, userId));
        Set<Object> articleLikeSet = redisService.getSet(USER_ARTICLE_LIKE + userId);
        Set<Object> commentLikeSet = redisService.getSet(USER_COMMENT_LIKE + userId);
        Set<Object> talkLikeSet = redisService.getSet(USER_TALK_LIKE + userId);
        return UserVo
                .builder()
                .id(userId)
                .avatar(user.getAvatar())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .email(user.getEmail())
                .webSite(user.getWebSite())
                .intro(user.getIntro())
                .articleLikeSet(articleLikeSet)
                .commentLikeSet(commentLikeSet)
                .talkLikeSet(talkLikeSet)
                .loginType(user.getLoginType())
                .build();
    }

    /**
     * 修改用户密码
     *
     * @param userPwdDto 用户信息
     */
    @Override
    public void updateUserPwd(UserPwdDto userPwdDto) {
        verifyMailOrCode(userPwdDto.getUsername(), userPwdDto.getCode());
        User existUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getUsername)
                .eq(User::getUsername, userPwdDto.getUsername()));
        Assert.notNull(existUser, "邮箱尚未注册!");
        // 根据用户名修改密码
        userMapper.update(new User(), new LambdaUpdateWrapper<User>()
                .set(User::getPassword, SecurityUtils.Encrypt(userPwdDto.getPassword(),userPwdDto.getUsername()))
                .eq(User::getUsername, userPwdDto.getUsername()));
    }

    /**
     * 获取后台登录用户信息
     *
     * @return
     */
    @Override
    public UserAdminInfoVO getUserInfo() {
        Integer userId = StpUtil.getLoginIdAsInt();
        // 查询用户信息
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getAvatar).eq(User::getId, userId));
        // 查询用户角色
        List<String> roleIdList = StpUtil.getRoleList();
        // 用户权限列表
        List<String> permissionList = StpUtil.getPermissionList().stream()
                .filter(string -> !string.isEmpty())
                .distinct()
                .collect(Collectors.toList());
        return UserAdminInfoVO.builder()
                .id(userId)
                .avatar(user.getAvatar())
                .roleList(roleIdList)
                .permissionList(permissionList)
                .build();
    }

    /**
     * 获取登录用户菜单
     *
     * @return
     */
    @Override
    public List<RouterVo> getUserMenu() {
        // 查询用户菜单
        List<UserMenuVo> userMenuVOList = menuMapper.selectMenuByUserId(StpUtil.getLoginIdAsInt());
        // 递归生成路由,parentId为0
        List<RouterVo> routerVoList = recurRoutes(PARENT_ID, userMenuVOList);
        return routerVoList;
    }

    /**
     * 获取后台登录用户信息
     *
     * @return 后台用户信息
     */
    @Override
    public UserBackInfoVo getUserBackInfo() {
        Integer userId = StpUtil.getLoginIdAsInt();
        // 查询用户信息
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getAvatar).eq(User::getId, userId));
        // 查询用户角色
        List<String> roleIdList = StpUtil.getRoleList();
        // 用户权限列表
        List<String> permissionList = StpUtil.getPermissionList().stream()
                .filter(string -> !string.isEmpty())
                .distinct()
                .collect(Collectors.toList());
        return UserBackInfoVo.builder()
                .id(userId)
                .avatar(user.getAvatar())
                .roleList(roleIdList)
                .permissionList(permissionList)
                .build();
    }

    /**
     * 查看后台用户列表
     *
     * @param condition 条件
     * @return 用户后台列表
     */
    @Override
    public PageResult<UserBackVO> listUserBackVO(ConditionDto condition) {
        // 查询后台用户数量
        Long count = userMapper.countUser(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台用户列表
        List<UserBackVO> userBackVOList = userMapper.listUserBackVO(getLimit(), getSize(), condition);
        return new PageResult<>(userBackVOList, count);
    }

    /**
     * 查看用户角色选项
     *
     * @return 用户角色选项
     */
    @Override
    public List<UserRoleVo> listUserRoleDTO() {
        // 查询角色列表
        return roleMapper.selectUserRoleList();
    }

    /**
     * 修改用户
     *
     * @param user 用户信息
     * @return {@link Result <>}
     */
    @Override
    public void updateUser(UserRoleDto user) {
// 更新用户信息
        User newUser = User.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .build();
        baseMapper.updateById(newUser);
        // 删除用户角色
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getId()));
        // 重新添加用户角色
        userRoleMapper.insertUserRole(user.getId(), user.getRoleIdList());
        // 删除Redis缓存中的角色
        SaSession sessionByLoginId = StpUtil.getSessionByLoginId(user.getId(), false);
        Optional.ofNullable(sessionByLoginId).ifPresent(saSession -> saSession.delete("Role_List"));
    }

    /**
     * 修改用户状态
     *
     * @param disable 禁用信息
     * @return {@link Result<>}
     */
    @Override
    public void updateUserStatus(DisableDto disable) {
        // 更新用户状态
        User newUser = User.builder()
                .id(disable.getId())
                .isDisable(disable.getIsDisable())
                .build();
        userMapper.updateById(newUser);
        if (disable.getIsDisable().equals(TRUE)) {
            // 先踢下线
            StpUtil.logout(disable.getId());
            // 再封禁账号
            StpUtil.disable(disable.getId(), 86400);
        } else {
            // 解除封禁
            StpUtil.untieDisable(disable.getId());
        }
    }

    /**
     * 查看在线用户
     *
     * @param condition 条件
     * @return 在线用户列表
     */
    @Override
    public PageResult<OnlineVO> listOnlineUser(ConditionDto condition) {
        // 查询所有会话token
        List<String> tokenList = StpUtil.searchTokenSessionId("", 0, -1, false);
        List<OnlineVO> onlineVOList = tokenList.stream()
                .map(token -> {
                    // 获取tokenSession
                    SaSession sessionBySessionId = StpUtil.getSessionBySessionId(token);
                    return (OnlineVO) sessionBySessionId.get(ONLINE_USER);
                })
                .filter(onlineVO -> StringUtils.isEmpty(condition.getKeyword()) || onlineVO.getNickname().contains(condition.getKeyword()))
                .sorted(Comparator.comparing(OnlineVO::getLoginTime).reversed())
                .collect(Collectors.toList());
        // 执行分页
        int fromIndex = getLimit().intValue();
        int size = getSize().intValue();
        int toIndex = onlineVOList.size() - fromIndex > size ? fromIndex + size : onlineVOList.size();
        List<OnlineVO> userOnlineList = onlineVOList.subList(fromIndex, toIndex);
        return new PageResult<>(userOnlineList, (long) onlineVOList.size());
    }

    /**
     * 下线用户
     *
     * @param token 在线token
     * @return
     */
    @Override
    public void kickOutUser(String token) {
        StpUtil.logoutByTokenValue(token);
    }

    @Override
    public void updateAdminPassword(PasswordDto password) {
        Integer userId = StpUtil.getLoginIdAsInt();
        // 查询旧密码是否正确
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, userId));
        Assert.notNull(user, "用户不存在");
        Assert.isTrue(SecurityUtils.checkPwd(user.getPassword(), password.getOldPassword(),user.getUsername()), "旧密码校验不通过!");
        // 正确则修改密码
        String newPassword = SecurityUtils.Encrypt(password.getNewPassword(),user.getUsername());
        user.setPassword(newPassword);
        userMapper.updateById(user);
    }

    /**
     * 校验邮箱验证码
     * @param username
     * @param code
     */
    public void verifyMailOrCode(String username,String code){
        String mailCode = redisService.getObject(CODE_KEY + username);
        Assert.notBlank(mailCode, "验证码未发送或已过期！");
        Assert.isTrue(mailCode.equals(code), "验证码错误，请重新输入！");
    }


    /**
     * 递归生成路由列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return 路由列表
     */
    private List<RouterVo> recurRoutes(Integer parentId, List<UserMenuVo> menuList) {
        List<RouterVo> list = new ArrayList<>();
        if (menuList != null) menuList.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .forEach(menu -> {
                    RouterVo routeVO = new RouterVo();
                    routeVO.setName(menu.getMenuName());
                    routeVO.setPath(getRouterPath(menu));
                    routeVO.setComponent(getComponent(menu));
                    routeVO.setMeta(MetaVo.builder()
                            .title(menu.getMenuName())
                            .icon(menu.getIcon())
                            .hidden(menu.getIsHidden().equals(TRUE))
                            .build());
                    if (menu.getMenuType().equals(TYPE_DIR)) {
                        List<RouterVo> children = recurRoutes(menu.getId(), menuList);
                        if (CollectionUtil.isNotEmpty(children)) {
                            routeVO.setAlwaysShow(true);
                            routeVO.setRedirect("noRedirect");
                        }
                        routeVO.setChildren(children);
                    } else if (isMenuFrame(menu)) {
                        routeVO.setMeta(null);
                        List<RouterVo> childrenList = new ArrayList<>();
                        RouterVo children = new RouterVo();
                        children.setName(menu.getMenuName());
                        children.setPath(menu.getPath());
                        children.setComponent(menu.getComponent());
                        children.setMeta(MetaVo.builder()
                                .title(menu.getMenuName())
                                .icon(menu.getIcon())
                                .hidden(menu.getIsHidden().equals(TRUE))
                                .build());
                        childrenList.add(children);
                        routeVO.setChildren(childrenList);
                    }
                    list.add(routeVO);
                });
        return list;
    }
    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(UserMenuVo menu) {
        String routerPath = menu.getPath();
        // 一级目录
        if (menu.getParentId().equals(PARENT_ID) && TYPE_DIR.equals(menu.getMenuType())) {
            routerPath = "/" + menu.getPath();
        }
        // 一级菜单
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(UserMenuVo menu) {
        String component = LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(UserMenuVo menu) {
        return menu.getParentId().equals(PARENT_ID) && TYPE_MENU.equals(menu.getMenuType());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(UserMenuVo menu) {
        return !menu.getParentId().equals(PARENT_ID) && TYPE_DIR.equals(menu.getMenuType());
    }
}




