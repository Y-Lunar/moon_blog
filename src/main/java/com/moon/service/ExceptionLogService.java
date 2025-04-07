package com.moon.service;

import com.moon.entity.ExceptionLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.vo.PageResult;

/**
* @author Y.0
* @description 针对表【t_exception_log】的数据库操作Service
* @createDate 2023-10-24 18:45:25
*/
public interface ExceptionLogService extends IService<ExceptionLog> {

    /**
     * 查看异常日志列表
     *
     * @param condition 条件
     * @return 日志列表
     */
    PageResult<ExceptionLog> listExceptionLog(ConditionDto condition);

    void saveExceptionLog(ExceptionLog exceptionLog);
}
