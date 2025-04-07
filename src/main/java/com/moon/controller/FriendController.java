package com.moon.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.moon.annotation.OptLogger;
import com.moon.annotation.VisitLogger;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.FriendDto;
import com.moon.entity.vo.FriendBackVo;
import com.moon.entity.vo.FriendVO;
import com.moon.entity.vo.PageResult;
import com.moon.service.FriendService;
import com.moon.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.moon.constant.OptTypeConstant.*;

/**
 * 友链管理模块
 *
 * @author:Y.0
 * @date:2023/10/13
 */

@Api(tags = "友链管理模块")
@RestController
public class FriendController {

    @Autowired
    private FriendService friendService;

    /**
     * 查看友链列表
     *
     * @return
     */
    @VisitLogger(value = "友链")
    @ApiOperation(value = "查看友链列表")
    @GetMapping("/friend/list")
    public Result<List<FriendVO>> FriendVoList() {
        return Result.success(friendService.FriendVoList());
    }

    /**
     * 查看友链后台列表
     *
     * @param condition 查询条件
     * @return
     */
    @ApiOperation(value = "查看友链后台列表")
    @SaCheckPermission("web:friend:list")
    @GetMapping("/admin/friend/list")
    public Result<PageResult<FriendBackVo>> listFriendBackVO(ConditionDto condition) {
        PageResult<FriendBackVo> friendBackVoPageResult = friendService.friendListVo(condition);
        return Result.success(friendBackVoPageResult);
    }

    /**
     * 添加友链
     *
     * @param friend 友链
     * @return
     */
    @OptLogger(value = ADD)
    @ApiOperation(value = "添加友链")
    @SaCheckPermission("web:friend:add")
    @PostMapping("/admin/friend/add")
    public Result<?> addFriend(@Validated @RequestBody FriendDto friend) {
        friendService.addFriend(friend);
        return Result.success();
    }

    /**
     * 删除友链
     *
     * @param friendIdList 友链id集合
     * @return {@link Result<>}
     */
    @OptLogger(value = DELETE)
    @ApiOperation(value = "删除友链")
    @SaCheckPermission("web:friend:delete")
    @DeleteMapping("/admin/friend/delete")
    public Result<?> deleteFriend(@RequestBody List<Integer> friendIdList) {
        friendService.removeByIds(friendIdList);
        return Result.success();
    }

    /**
     * 修改友链
     *
     * @param friend 友链
     * @return
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "修改友链")
    @SaCheckPermission("web:friend:update")
    @PutMapping("/admin/friend/update")
    public Result<?> updateFriend(@Validated @RequestBody FriendDto friend) {
        friendService.updateFriend(friend);
        return Result.success();
    }

}
