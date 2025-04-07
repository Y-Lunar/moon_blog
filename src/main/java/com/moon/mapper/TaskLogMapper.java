package com.moon.mapper;

import com.moon.entity.TaskLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.vo.TaskLogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_task_log】的数据库操作Mapper
* @createDate 2023-10-24 18:45:10
* @Entity com.moon.entity.TaskLog
*/
public interface TaskLogMapper extends BaseMapper<TaskLog> {


    /**
     * 查询定时任务日志数量
     *
     * @param condition 条件
     * @return 定时任务日志数量
     */
    Long selectTaskLogCount(@Param("condition") ConditionDto condition);

    /**
     * 查询定时任务日志列表
     *
     * @param limit     页码
     * @param size      大小
     * @param condition 条件
     * @return 定时任务日志列表
     */
    List<TaskLogVO> selectTaskLogVOList(@Param("limit") Long limit, @Param("size") Long size, @Param("condition") ConditionDto condition);
}




