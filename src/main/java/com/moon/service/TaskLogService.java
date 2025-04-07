package com.moon.service;

import com.moon.entity.TaskLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.vo.PageResult;
import com.moon.entity.vo.TaskLogVO;

/**
* @author Y.0
* @description 针对表【t_task_log】的数据库操作Service
* @createDate 2023-10-24 18:45:10
*/
public interface TaskLogService extends IService<TaskLog> {

    /**
     * 查看定时任务日志
     *
     * @param condition 条件
     * @return 后台定时任务日志
     */
    PageResult<TaskLogVO> listTaskLog(ConditionDto condition);

    /**
     * 清空定时任务日志
     *
     * @return
     */
    void clearTaskLog();
}
