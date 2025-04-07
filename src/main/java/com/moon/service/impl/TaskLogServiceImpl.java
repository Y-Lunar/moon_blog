package com.moon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.entity.TaskLog;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.vo.PageResult;
import com.moon.entity.vo.TaskLogVO;
import com.moon.service.TaskLogService;
import com.moon.mapper.TaskLogMapper;
import com.moon.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_task_log】的数据库操作Service实现
* @createDate 2023-10-24 18:45:10
*/
@Service
public class TaskLogServiceImpl extends ServiceImpl<TaskLogMapper, TaskLog>
    implements TaskLogService{

    @Autowired(required = false)
    private TaskLogMapper taskLogMapper;

    @Override
    public PageResult<TaskLogVO> listTaskLog(ConditionDto condition) {
        // 查询定时任务日志数量
        Long count = taskLogMapper.selectTaskLogCount(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询定时任务日志列表
        List<TaskLogVO> taskLogVOList = taskLogMapper.selectTaskLogVOList(PageUtils.getLimit(), PageUtils.getSize(), condition);
        return new PageResult<>(taskLogVOList, count);
    }

    @Override
    public void clearTaskLog() {
        taskLogMapper.delete(null);
    }
}




