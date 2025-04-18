package com.moon.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.moon.annotation.OptLogger;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.MenuDTO;
import com.moon.entity.vo.MenuOption;
import com.moon.entity.vo.MenuTree;
import com.moon.entity.vo.MenuVO;
import com.moon.service.MenuService;
import com.moon.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.moon.constant.OptTypeConstant.*;


/**
 * 菜单控制器
 *
 * @author:Y.0
 * @date:2023/10/31
 **/
@Api(tags = "菜单模块")
@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 查看菜单列表
     *
     * @return 菜单列表
     */
    @ApiOperation(value = "查看菜单列表")
    @SaCheckPermission("system:menu:list")
    @GetMapping("/admin/menu/list")
    public Result<List<MenuVO>> listMenuVO(ConditionDto condition) {
        return Result.success(menuService.listMenuVO(condition));
    }

    /**
     * 添加菜单
     *
     * @return
     */
    @OptLogger(value = ADD)
    @ApiOperation(value = "添加菜单")
    @SaCheckPermission("system:menu:add")
    @PostMapping("/admin/menu/add")
    public Result<?> addMenu(@Validated @RequestBody MenuDTO menu) {
        menuService.addMenu(menu);
        return Result.success();
    }

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     * @return {@link Result<>}
     */
    @OptLogger(value = DELETE)
    @ApiOperation(value = "删除菜单")
    @SaCheckPermission("system:menu:delete")
    @DeleteMapping("/admin/menu/delete/{menuId}")
    public Result<?> deleteMenu(@PathVariable("menuId") Integer menuId) {
        menuService.deleteMenu(menuId);
        return Result.success();
    }

    /**
     * 修改菜单
     *
     * @return {@link Result<>}
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "修改菜单")
    @SaCheckPermission("system:menu:update")
    @PutMapping("/admin/menu/update")
    public Result<?> updateMenu(@Validated @RequestBody MenuDTO menu) {
        menuService.updateMenu(menu);
        return Result.success();
    }

    /**
     * 获取菜单下拉树
     *
     * @return 菜单树
     */
    @ApiOperation(value = "获取菜单下拉树")
    @SaCheckPermission("system:role:list")
    @GetMapping("/admin/menu/getMenuTree")
    public Result<List<MenuTree>> listMenuTree() {
        return Result.success(menuService.listMenuTree());
    }

    /**
     * 获取菜单选项树
     *
     * @return 菜单下拉树
     */
    @ApiOperation(value = "获取菜单选项树")
    @SaCheckPermission("system:menu:list")
    @GetMapping("/admin/menu/getMenuOptions")
    public Result<List<MenuOption>> listMenuOption() {
        return Result.success(menuService.listMenuOption());
    }

    /**
     * 编辑菜单
     *
     * @param menuId 菜单id
     * @return 菜单
     */
    @ApiOperation(value = "编辑菜单")
    @SaCheckPermission("system:menu:edit")
    @GetMapping("/admin/menu/edit/{menuId}")
    public Result<MenuDTO> editMenu(@PathVariable("menuId") Integer menuId) {
        return Result.success(menuService.editMenu(menuId));
    }

}