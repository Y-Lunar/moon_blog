package com.moon.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.constant.ScheduleConstant;
import com.moon.entity.Task;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.StatusDTO;
import com.moon.entity.dto.TaskDTO;
import com.moon.entity.dto.TaskRunDTO;
import com.moon.entity.vo.PageResult;
import com.moon.entity.vo.TaskBackVO;
import com.moon.enums.TaskStatusEnum;
import com.moon.exception.ServiceException;
import com.moon.quartz.utils.CronUtils;
import com.moon.quartz.utils.ScheduleUtils;
import com.moon.service.TaskService;
import com.moon.mapper.TaskMapper;
import com.moon.utils.BeanCopyUtils;
import com.moon.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * 定时任务业务接口实现类
 *
* @author Y.0
* @description 针对表【t_task】的数据库操作Service实现
* @createDate 2023-10-14 11:41:25
*/
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task>
    implements TaskService{

    @Autowired
    private Scheduler scheduler;

    @Autowired(required = false)
    private TaskMapper taskMapper;

    /**
     * 项目启动时，初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理
     * 注：不能手动修改数据库ID和任务组名，否则会导致脏数据
     */
    @PostConstruct
    public void init() throws SchedulerException {
        scheduler.clear();
        List<Task> taskList = taskMapper.selectList(null);
        for (Task task : taskList) {
            // 创建定时任务
            ScheduleUtils.createScheduleJob(scheduler, task);
        }
    }

    @Override
    public PageResult<TaskBackVO> listTaskBackVO(ConditionDto condition) {
        // 查询定时任务数量
        Long count = taskMapper.countTaskBackVO(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询定时任务列表
        List<TaskBackVO> taskBackVOList = taskMapper.selectTaskBackVO(PageUtils.getLimit(), PageUtils.getSize(), condition);
        taskBackVOList.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getCronExpression())) {
                Date nextExecution = CronUtils.getNextExecution(item.getCronExpression());
                item.setNextValidTime(nextExecution);
            } else {
                item.setNextValidTime(null);
            }
        });
        return new PageResult<>(taskBackVOList, count);
    }

    @Override
    public void addTask(TaskDTO task) {
        Assert.isTrue(CronUtils.isValid(task.getCronExpression()), "Cron表达式无效");
        Task newTask = BeanCopyUtils.copyBean(task, Task.class);
        // 新增定时任务
        int row = taskMapper.insert(newTask);
        // 创建定时任务
        if (row > 0) {
            ScheduleUtils.createScheduleJob(scheduler, newTask);
        }
    }

    @Override
    public void updateTask(TaskDTO task) {
        Assert.isTrue(CronUtils.isValid(task.getCronExpression()), "Cron表达式无效");
        Task existTask = taskMapper.selectById(task.getId());
        Task newTask = BeanCopyUtils.copyBean(task, Task.class);
        // 更新定时任务
        int row = taskMapper.updateById(newTask);
        if (row > 0) {
            try {
                updateSchedulerJob(newTask, existTask.getTaskGroup());
            } catch (SchedulerException e) {
                throw new ServiceException("更新定时任务异常");
            }
        }
    }

    @Override
    public void deleteTask(List<Integer> taskIdList) {
        List<Task> taskList = taskMapper.selectList(new LambdaQueryWrapper<Task>()
                .select(Task::getId, Task::getTaskGroup)
                .in(Task::getId, taskIdList));
        // 删除定时任务
        int row = taskMapper.delete(new LambdaQueryWrapper<Task>().in(Task::getId, taskIdList));
        if (row > 0) {
            taskList.forEach(task -> {
                try {
                    scheduler.deleteJob(ScheduleUtils.getJobKey(task.getId(), task.getTaskGroup()));
                } catch (SchedulerException e) {
                    throw new ServiceException("删除定时任务异常");
                }
            });
        }
    }

    @Override
    public void updateTaskStatus(StatusDTO taskStatus) {
        Task task = taskMapper.selectById(taskStatus.getId());
        // 相同不用更新
        if (task.getStatus().equals(taskStatus.getStatus())) {
            return;
        }
        // 更新数据库中的定时任务状态
        Task newTask = Task.builder()
                .id(taskStatus.getId())
                .status(taskStatus.getStatus())
                .build();
        int row = taskMapper.updateById(newTask);
        // 获取定时任务状态、id、任务组
        Integer status = taskStatus.getStatus();
        Integer taskId = task.getId();
        String taskGroup = task.getTaskGroup();
        if (row > 0) {
            // 更新定时任务
            try {
                if (TaskStatusEnum.RUNNING.getStatus().equals(status)) {
                    scheduler.resumeJob(ScheduleUtils.getJobKey(taskId, taskGroup));
                }
                if (TaskStatusEnum.PAUSE.getStatus().equals(status)) {
                    scheduler.pauseJob(ScheduleUtils.getJobKey(taskId, taskGroup));
                }
            } catch (SchedulerException e) {
                throw new ServiceException("更新定时任务状态异常");
            }
        }
    }

    @Override
    public void runTask(TaskRunDTO taskRun) {
        Integer taskId = taskRun.getId();
        String taskGroup = taskRun.getTaskGroup();
        // 查询定时任务
        Task task = taskMapper.selectById(taskRun.getId());
        // 设置参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstant.TASK_PROPERTIES, task);
        try {
            scheduler.triggerJob(ScheduleUtils.getJobKey(taskId, taskGroup), dataMap);
        } catch (SchedulerException e) {
            throw new ServiceException("执行定时任务异常");
        }
    }

    /**
     * 更新任务
     *
     * @param task      任务对象
     * @param taskGroup 任务组名
     */
    public void updateSchedulerJob(Task task, String taskGroup) throws SchedulerException {
        Integer taskId = task.getId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(taskId, taskGroup);
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, task);
    }

}




