package com.moon.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.moon.annotation.OptLogger;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.RoleDto;
import com.moon.entity.dto.RoleStatusDto;
import com.moon.entity.vo.PageResult;
import com.moon.entity.vo.RoleVo;
import com.moon.service.RoleService;
import com.moon.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.moon.constant.OptTypeConstant.*;

/**
 * 角色管理模块
 *
 * @author:Y.0
 * @date:2023/10/24
 */

@Api(tags = "角色管理模块")
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 查看角色列表
     *
     * @param condition 查询条件
     * @return 角色列表
     */
    @ApiOperation(value = "查看角色列表")
    @SaCheckPermission("system:role:list")
    @GetMapping("/admin/role/list")
    public Result<PageResult<RoleVo>> listRoleVO(ConditionDto condition) {
        return Result.success(roleService.roleListVo(condition));
    }

    /**
     * 添加角色
     *
     * @param role 角色信息
     * @return
     */
    @OptLogger(value = ADD)
    @ApiOperation(value = "添加角色")
    @SaCheckPermission("system:role:add")
    @PostMapping("/admin/role/add")
    public Result<?> addRole(@Validated @RequestBody RoleDto role) {
        roleService.addRole(role);
        return Result.success();
    }

    /**
     * 删除角色
     *
     * @param roleIdList 角色id集合
     * @return {@link Result<>}
     */
    @OptLogger(value = DELETE)
    @ApiOperation(value = "删除角色")
    @SaCheckPermission("system:role:delete")
    @DeleteMapping("/admin/role/delete")
    public Result<?> deleteRole(@RequestBody List<String> roleIdList) {
        roleService.deleteRole(roleIdList);
        return Result.success();
    }

    /**
     * 修改角色
     *
     * @param role 角色信息
     * @return
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "修改角色")
    @SaCheckPermission("system:role:update")
    @PutMapping("/admin/role/update")
    public Result<?> updateRole(@Validated @RequestBody RoleDto role) {
        roleService.updateRole(role);
        return Result.success();
    }

    /**
     * 修改角色状态
     *
     * @param roleStatus 角色状态
     * @return
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "修改角色状态")
    @SaCheckPermission(value = {"system:role:update", "system:role:status"}, mode = SaMode.OR)
    @PutMapping("/admin/role/changeStatus")
    public Result<?> updateRoleStatus(@Validated @RequestBody RoleStatusDto roleStatus) {
        roleService.updateRoleStatus(roleStatus);
        return Result.success();
    }

    /**
     * 查看角色的菜单权限
     *
     * @param roleId 角色id
     * @return {@link List<Integer>} 角色的菜单权限
     */
    @ApiOperation(value = "查看角色的菜单权限")
    @SaCheckPermission("system:role:list")
    @GetMapping("/admin/role/menu/{roleId}")
    public Result<List<Integer>> listRoleMenuTree(@PathVariable("roleId") String roleId) {
        return Result.success(roleService.listRoleMenuTree(roleId));
    }

}
