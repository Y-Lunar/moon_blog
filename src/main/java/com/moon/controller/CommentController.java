package com.moon.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.moon.annotation.AccessLimit;
import com.moon.annotation.OptLogger;
import com.moon.entity.dto.CheckDto;
import com.moon.entity.dto.CommentDto;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.vo.*;
import com.moon.enums.LikeTypeEnum;
import com.moon.service.CommentService;
import com.moon.strategy.context.LikeStrategyContext;
import com.moon.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.moon.constant.OptTypeConstant.DELETE;
import static com.moon.constant.OptTypeConstant.UPDATE;

/**
 * 评论管理模块
 *
 * @author:Y.0
 * @date:2023/10/12
 */

@Api(tags = "评论模块管理")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeStrategyContext likeStrategyContext;
    /**
     * 查看最新评论（top 5）
     *
     * @return
     */
    @ApiOperation("首页查看最新评论")
    @GetMapping("/recent/comment")
    public Result<List<RecentCommentVo>> RecentCommentList(){
        return Result.success(commentService.recentCommentList());
    }

    /**
     * 查看评论
     *
     * @param condition 条件
     * @return
     */
    @ApiOperation(value = "查看评论")
    @GetMapping("/comment/list")
    public Result<PageResult<CommentVo>> CommentVoList(ConditionDto condition) {
        return Result.success(commentService.CommentVoList(condition));
    }

    /**
     * 查看回复评论
     *
     * @param commentId 评论id
     * @return
     */
    @ApiOperation("查看回复评论")
    @GetMapping("/comment/{commentId}/reply")
    public Result<List<RelyVo>> listResult(@PathVariable("commentId") Integer commentId){
        return Result.success(commentService.listResultById(commentId));
    }

    /**
     * 查看后台评论列表
     *
     * @param condition 条件
     * @return 后台评论
     */
    @ApiOperation(value = "查看后台评论")
    @SaCheckPermission("news:comment:list")
    @GetMapping("/admin/comment/list")
    public Result<PageResult<CommentBackVo>> listCommentBackVO(ConditionDto condition) {
        return Result.success(commentService.commentListVo(condition));
    }

    /**
     * 添加评论
     *
     * @param comment 评论信息
     * @return
     */
    @SaCheckLogin
    @ApiOperation(value = "添加评论")
    @SaCheckPermission("news:comment:add")
    @PostMapping("/comment/add")
    public Result<?> addComment(@Validated @RequestBody CommentDto comment) {
        commentService.addComment(comment);
        return Result.success();
    }

    /**
     * 删除评论
     *
     * @param commentIdList 评论id
     * @return {@link Result<>}
     */
    @OptLogger(value = DELETE)
    @ApiOperation(value = "删除评论")
    @SaCheckPermission("news:comment:delete")
    @DeleteMapping("/admin/comment/delete")
    public Result<?> deleteComment(@RequestBody List<Integer> commentIdList) {
        commentService.removeByIds(commentIdList);
        return Result.success();
    }

    /**
     * 审核评论
     *
     * @param check 审核信息
     * @return
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "审核评论")
    @SaCheckPermission("news:comment:pass")
    @PutMapping("/admin/comment/pass")
    public Result<?> updateCommentCheck(@Validated @RequestBody CheckDto check) {
        commentService.updateCommentCheck(check);
        return Result.success();
    }

    /**
     * 点赞评论
     *
     * @param commentId 评论id
     * @return {@link Result<>}
     */
    @SaCheckLogin
    @ApiOperation(value = "点赞评论")
    @AccessLimit(seconds = 60, maxCount = 3)
    @SaCheckPermission("news:comment:like")
    @PostMapping("/comment/{commentId}/like")
    public Result<?> likeComment(@PathVariable("commentId") Integer commentId) {
        likeStrategyContext.executeLikeStrategy(LikeTypeEnum.COMMENT, commentId);
        return Result.success();
    }
}
