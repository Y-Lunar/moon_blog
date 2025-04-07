package com.moon.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.moon.annotation.AccessLimit;
import com.moon.annotation.OptLogger;
import com.moon.annotation.VisitLogger;
import com.moon.entity.dto.CheckDto;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.MessageDto;
import com.moon.entity.vo.MessageBackVO;
import com.moon.entity.vo.MessageVo;
import com.moon.entity.vo.PageResult;
import com.moon.service.MessageService;
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
 * 留言管理模块
 *
 * @author:Y.0
 * @date:2023/10/13
 */
@Api(tags = "留言管理模块")
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 查看留言列表
     *
     * @return
     */
    @VisitLogger(value = "留言")
    @ApiOperation("查看留言列表")
    @GetMapping("/message/list")
    public Result<List<MessageVo>> messageList(){
        return Result.success(messageService.messageList());
    }

    /**
     * 添加留言
     *
     * @param messageDto 留言信息
     * @return
     */
    @ApiOperation("添加留言")
    @AccessLimit(seconds = 60, maxCount = 3)
    @PostMapping("/message/add")
    public Result<?> insertMessage(@Validated @RequestBody MessageDto messageDto){
        messageService.insertMessage(messageDto);
        return Result.success();
    }

    /**
     * 查看后台留言列表
     *
     * @param condition 条件
     * @return 留言列表
     */
    @ApiOperation(value = "查看后台留言列表")
    @SaCheckPermission("news:message:list")
    @GetMapping("/admin/message/list")
    public Result<PageResult<MessageBackVO>> listMessageBackVO(ConditionDto condition) {
        return Result.success(messageService.messageListVo(condition));
    }


    /**
     * 删除留言
     *
     * @param messageIdList 留言Id列表
     * @return
     */
    @OptLogger(value = DELETE)
    @ApiOperation(value = "删除留言")
    @SaCheckPermission("news:message:delete")
    @DeleteMapping("/admin/message/delete")
    public Result<?> deleteMessage(@RequestBody List<Integer> messageIdList) {
        messageService.removeByIds(messageIdList);
        return Result.success();
    }

    /**
     * 审核留言
     *
     * @param check 审核信息
     * @return
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "审核留言")
    @SaCheckPermission("news:message:pass")
    @PutMapping("/admin/message/pass")
    public Result<?> updateMessageCheck(@Validated @RequestBody CheckDto check) {
        messageService.updateMessageCheck(check);
        return Result.success();
    }

}
