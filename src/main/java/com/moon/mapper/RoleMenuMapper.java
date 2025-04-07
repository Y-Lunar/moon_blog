package com.moon.mapper;

import com.moon.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_role_menu】的数据库操作Mapper
* @createDate 2023-09-21 16:17:17
* @Entity com.moon.entity.RoleMenu
*/
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * 添加角色菜单
     *
     * @param id         角色id
     * @param menuIdList 菜单id集合
     */
    void insertRoleMenu(@Param("roleId") String id, List<Integer> menuIdList);

    /**
     * 批量删除角色菜单
     *
     * @param roleIdList 需要删除的数据ID
     */
    void deleteRoleMenu(List<String> roleIdList);

    /**
     * 根据角色id删除角色菜单
     *
     * @param id 角色id
     */
    void deleteRoleMenuByRoleId(@Param("roleId") String id);

    /**
     * 根据角色id查询菜单权限
     *
     * @param roleId 角色id
     * @return 菜单权限
     */
    List<Integer> selectMenuByRoleId(String roleId);
}




