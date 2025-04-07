package com.moon.mapper;

import com.moon.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_user_role】的数据库操作Mapper
* @createDate 2023-09-21 16:17:07
* @Entity com.moon.entity.UserRole
*/
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 添加用户角色
     *
     * @param userId     用户id
     * @param roleIdList 角色id集合
     */
    void insertUserRole(@Param("userId") Integer userId, @Param("roleIdList") List<String> roleIdList);
}




