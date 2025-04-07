package com.moon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.entity.UserRole;
import com.moon.service.UserRoleService;
import com.moon.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author Y.0
* @description 针对表【t_user_role】的数据库操作Service实现
* @createDate 2023-09-21 16:17:07
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




