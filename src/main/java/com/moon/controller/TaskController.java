package com.moon.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.moon.annotation.OptLogger;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.StatusDTO;
import com.moon.entity.dto.TaskDTO;
import com.moon.entity.dto.TaskRunDTO;
import com.moon.entity.vo.PageResult;
import com.moon.entity.vo.TaskBackVO;
import com.moon.service.TaskService;
import com.moon.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.moon.constant.OptTypeConstant.*;


/**
 * 定时任务模块管理
 *
 * @author:Y.0
 * @date:2023/10/14
 */

@Api(tags = "定时任务模块管理")
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * 查看定时任务列表
     *
     * @param condition 条件
     * @return {@link TaskBackVO} 定时任务列表
     */
    @ApiOperation("查看定时任务列表")
    @SaCheckPermission("monitor:task:list")
    @GetMapping("/admin/task/list")
    public Result<PageResult<TaskBackVO>> listTaskBackVO(ConditionDto condition) {
        return Result.success(taskService.listTaskBackVO(condition));
    }

    /**
     * 添加定时任务
     *
     * @param task 定时任务信息
     * @return {@link Result<>}
     */
    @OptLogger(value = ADD)
    @ApiOperation("添加定时任务")
    @SaCheckPermission("monitor:task:add")
    @PostMapping("/admin/task/add")
    public Result<?> addTask(@Validated @RequestBody TaskDTO task) {
        taskService.addTask(task);
        return Result.success();
    }

    /**
     * 修改定时任务
     *
     * @param task 定时任务信息
     * @return {@link Result<>}
     */
    @OptLogger(value = UPDATE)
    @ApiOperation("修改定时任务")
    @SaCheckPermission(value = "monitor:task:update")
    @PutMapping("/admin/task/update")
    public Result<?> updateTask(@Validated @RequestBody TaskDTO task) {
        taskService.updateTask(task);
        return Result.success();
    }

    /**
     * 删除定时任务
     *
     * @param taskIdList 定时任务id集合
     * @return {@link Result<>}
     */
    @OptLogger(value = DELETE)
    @ApiOperation("删除定时任务")
    @SaCheckPermission("monitor:task:delete")
    @DeleteMapping("/admin/task/delete")
    public Result<?> deleteTask(@RequestBody List<Integer> taskIdList) {
        taskService.deleteTask(taskIdList);
        return Result.success();
    }

    /**
     * 修改定时任务状态
     *
     * @param taskStatus 定时任务状态
     * @return {@link Result<>}
     */
    @OptLogger(value = UPDATE)
    @ApiOperation("修改定时任务状态")
    @SaCheckPermission(value = {"monitor:task:update", "monitor:task:status"}, mode = SaMode.OR)
    @PutMapping("/admin/task/changeStatus")
    public Result<?> updateTaskStatus(@Validated @RequestBody StatusDTO taskStatus) {
        taskService.updateTaskStatus(taskStatus);
        return Result.success();
    }

    /**
     * 执行定时任务
     *
     * @param taskRun 运行定时任务
     * @return {@link Result<>}
     */
    @OptLogger(value = UPDATE)
    @ApiOperation("执行定时任务")
    @SaCheckPermission("monitor:task:run")
    @PutMapping("/admin/task/run")
    public Result<?> runTask(@RequestBody TaskRunDTO taskRun) {
        taskService.runTask(taskRun);
        return Result.success();
    }
}
