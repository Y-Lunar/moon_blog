package com.moon.service;

import com.moon.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.RoleDto;
import com.moon.entity.dto.RoleStatusDto;
import com.moon.entity.vo.PageResult;
import com.moon.entity.vo.RoleVo;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_role】的数据库操作Service
* @createDate 2023-09-21 16:17:13
*/
public interface RoleService extends IService<Role> {
    /**
     * 查看角色列表
     *
     * @param condition 查询条件
     * @return 角色列表
     */
    PageResult<RoleVo> roleListVo(ConditionDto condition);

    /**
     * 添加角色
     *
     * @param role 角色信息
     */
    void addRole(RoleDto role);

    /**
     * 删除角色
     *
     * @param roleIdList 角色id集合
     */
    void deleteRole(List<String> roleIdList);

    /**
     * 修改角色
     *
     * @param role 角色信息
     */
    void updateRole(RoleDto role);

    /**
     * 修改角色状态
     *
     * @param roleStatus 角色状态
     */
    void updateRoleStatus(RoleStatusDto roleStatus);

    /**
     * 查看角色的菜单权限
     *
     * @param roleId 角色id
     * @return 角色的菜单权限
     */
    List<Integer> listRoleMenuTree(String roleId);
}
