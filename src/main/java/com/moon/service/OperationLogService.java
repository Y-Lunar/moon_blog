package com.moon.service;

import com.moon.entity.OperationLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.vo.OperationLogVO;
import com.moon.entity.vo.PageResult;

/**
* @author Y.0
* @description 针对表【t_operation_log】的数据库操作Service
* @createDate 2023-10-24 18:45:21
*/
public interface OperationLogService extends IService<OperationLog> {

    /**
     * 查看操作日志列表
     *
     * @param condition 条件
     * @return 日志列表
     */
    PageResult<OperationLogVO> listOperationLogVO(ConditionDto condition);

    void saveOperationLog(OperationLog operationLog);
}
