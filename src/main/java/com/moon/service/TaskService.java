package com.moon.service;

import com.moon.entity.Task;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.StatusDTO;
import com.moon.entity.dto.TaskDTO;
import com.moon.entity.dto.TaskRunDTO;
import com.moon.entity.vo.PageResult;
import com.moon.entity.vo.TaskBackVO;

import java.util.List;

/**
 * 定时任务业务接口
 *
* @author Y.0
* @description 针对表【t_task】的数据库操作Service
* @createDate 2023-10-14 11:41:25
*/
public interface TaskService extends IService<Task> {
    /**
     * 查看定时任务列表
     *
     * @param condition 条件
     * @return 定时任务列表
     */
    PageResult<TaskBackVO> listTaskBackVO(ConditionDto condition);

    /**
     * 添加定时任务
     *
     * @param task 定时任务
     */
    void addTask(TaskDTO task);

    /**
     * 修改定时任务
     *
     * @param task 定时任务信息
     */
    void updateTask(TaskDTO task);

    /**
     * 删除定时任务
     *
     * @param taskIdList 定时任务id集合
     */
    void deleteTask(List<Integer> taskIdList);

    /**
     * 修改定时任务状态
     *
     * @param taskStatus 定时任务状态
     */
    void updateTaskStatus(StatusDTO taskStatus);

    /**
     * 定时任务立即执行一次
     *
     * @param taskRun 定时任务
     */
    void runTask(TaskRunDTO taskRun);
}
