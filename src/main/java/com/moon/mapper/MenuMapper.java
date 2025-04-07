package com.moon.mapper;

import com.moon.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.MenuDTO;
import com.moon.entity.vo.MenuOption;
import com.moon.entity.vo.MenuTree;
import com.moon.entity.vo.MenuVO;
import com.moon.entity.vo.UserMenuVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_menu】的数据库操作Mapper
* @createDate 2023-10-24 19:03:59
* @Entity com.moon.entity.Menu
*/
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 查询菜单下拉树
     *
     * @return 菜单下拉树
     */
    List<MenuTree> selectMenuTree();

    /**
     * 查询菜单选项树
     *
     * @return 菜单选项树
     */
    List<MenuOption> selectMenuOptions();

    /**
     * 根据id查询菜单信息
     *
     * @param menuId 菜单id
     * @return 菜单
     */
    MenuDTO selectMenuById(@Param("menuId") Integer menuId);

    /**
     * 查询菜单列表
     *
     * @param condition 查询条件
     * @return 菜单列表
     */
    List<MenuVO> selectMenuVOList(ConditionDto condition);

    /**
     * 根据用户id查询用户菜单列表
     *
     * @param userId 用户id
     * @return 用户菜单列表
     */
    List<UserMenuVo> selectMenuByUserId(@Param("userId") Integer userId);


    /**
     * 根据角色id查询对应权限
     *
     * @param roleId id
     * @return 权限标识
     */
    List<String> selectPermissionByRoleId(@Param("roleId") String roleId);
}




