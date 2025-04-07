package com.moon.mapper;

import com.moon.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.vo.RoleVo;
import com.moon.entity.vo.UserRoleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_role】的数据库操作Mapper
* @createDate 2023-09-21 16:17:13
* @Entity com.moon.entity.Role
*/
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleListByLoginId(@Param("userId") Object loginId);

    /**
     * 查询后台角色数量
     *
     * @param condition 查询条件
     * @return 后台角色数量
     */
    Long selectCountRoleVO(@Param("condition") ConditionDto condition);

    /**
     * 查询后台角色列表
     *
     * @param limit     页码
     * @param size      大小
     * @param condition 查询条件
     * @return 后台角色列表
     */
    List<RoleVo> selectRoleVOList(@Param("limit") Long limit, @Param("size") Long size, @Param("condition") ConditionDto condition);

    /**
     * 查询用户角色选项
     *
     * @return 用户角色选项
     */
    List<UserRoleVo> selectUserRoleList();
}




