package com.moon.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.moon.annotation.AccessLimit;
import com.moon.annotation.OptLogger;
import com.moon.annotation.VisitLogger;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.TalkDto;
import com.moon.entity.vo.PageResult;
import com.moon.entity.vo.TalkBackInfoVo;
import com.moon.entity.vo.TalkBackVo;
import com.moon.entity.vo.TalkVo;
import com.moon.enums.LikeTypeEnum;
import com.moon.service.TalkService;
import com.moon.strategy.context.LikeStrategyContext;
import com.moon.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.moon.constant.OptTypeConstant.*;

/**
 * 说说管理模块
 *
 * @author:Y.0
 * @date:2023/10/8
 */

@Api(tags = "说说模块")
@RestController
public class TalkController {

    @Autowired
    private TalkService talkService;

    @Autowired
    private LikeStrategyContext likeStrategyContext;

    /**
     * 博客主界面说说
     * @return
     */
    @ApiOperation(value = "博客界面说说")
    @GetMapping("/home/talk")
    public Result<List<String>> talkList(){
        return Result.success(talkService.talkList());
    }

    /**
     * 查看说说列表
     *
     * @return
     */
    @VisitLogger(value = "说说列表")
    @ApiOperation("查看说说列表")
    @GetMapping("/talk/list")
    public Result<PageResult<TalkVo>> talkPageList(){
        PageResult<TalkVo> talkVoPageResult = talkService.talkPageList();
        return Result.success(talkVoPageResult);
    }

    /**
     * 查看说说
     *
     * @param talkId 说说id
     * @return
     */
    @VisitLogger(value = "说说")
    @ApiOperation(value = "查看说说")
    @GetMapping("/talk/{talkId}")
    public Result<TalkVo> getTalkById(@PathVariable("talkId") Integer talkId) {
        return Result.success(talkService.getTalkById(talkId));
    }

    /**
     * 点赞说说
     *
     * @param talkId 说说id
     * @return
     */
    @SaCheckLogin
    @ApiOperation(value = "点赞说说")
    @AccessLimit(seconds = 60, maxCount = 3)
    @SaCheckPermission("web:talk:like")
    @PostMapping("/talk/{talkId}/like")
    public Result<?> saveTalkLike(@PathVariable("talkId") Integer talkId) {
        likeStrategyContext.executeLikeStrategy(LikeTypeEnum.TALK, talkId);
        return Result.success();
    }

    /**
     * 查看后台说说列表
     *
     * @param condition 条件
     * @return 后台说说
     */
    @ApiOperation(value = "查看后台说说列表")
    @SaCheckPermission("web:talk:list")
    @GetMapping("/admin/talk/list")
    public Result<PageResult<TalkBackVo>> listTalkBackVO(ConditionDto condition) {
        return Result.success(talkService.talkListBackVo(condition));
    }

    /**
     * 上传说说图片
     *
     * @param file 文件
     * @return
     */
    @OptLogger(value = UPLOAD)
    @ApiOperation(value = "上传说说图片")
    @ApiImplicitParam(name = "file", value = "相册封面", required = true, dataType = "MultipartFile")
    @SaCheckPermission("web:talk:upload")
    @PostMapping("/admin/talk/upload")
    public Result<String> uploadTalkCover(@RequestParam("file") MultipartFile file) {
        return Result.success(talkService.uploadTalkCover(file));
    }

    /**
     * 添加说说
     *
     * @param talk 说说信息
     * @return
     */
    @OptLogger(value = ADD)
    @ApiOperation(value = "添加说说")
    @SaCheckPermission("web:talk:add")
    @PostMapping("/admin/talk/add")
    public Result<?> addTalk(@Validated @RequestBody TalkDto talk) {
        talkService.addTalk(talk);
        return Result.success();
    }

    /**
     * 删除说说
     *
     * @param talkId 说说id
     * @return
     */
    @OptLogger(value = DELETE)
    @ApiOperation(value = "删除说说")
    @SaCheckPermission("web:talk:delete")
    @DeleteMapping("/admin/talk/delete/{talkId}")
    public Result<?> deleteTalk(@PathVariable("talkId") Integer talkId) {
        talkService.deleteTalk(talkId);
        return Result.success();
    }

    /**
     * 修改说说
     *
     * @param talk 说说信息
     * @return
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "修改说说")
    @SaCheckPermission("web:talk:update")
    @PutMapping("/admin/talk/update")
    public Result<?> updateTalk(@Validated @RequestBody TalkDto talk) {
        talkService.updateTalk(talk);
        return Result.success();
    }

    /**
     * 编辑说说
     *
     * @param talkId 说说id
     * @return
     */
    @ApiOperation(value = "编辑说说")
    @SaCheckPermission("web:talk:edit")
    @GetMapping("/admin/talk/edit/{talkId}")
    public Result<TalkBackInfoVo> editTalk(@PathVariable("talkId") Integer talkId) {
        return Result.success(talkService.editTalk(talkId));
    }
}
