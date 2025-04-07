package com.moon.service;

import com.moon.entity.VisitLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.vo.PageResult;

/**
* @author Y.0
* @description 针对表【t_visit_log】的数据库操作Service
* @createDate 2023-10-24 18:45:03
*/
public interface VisitLogService extends IService<VisitLog> {

    void saveVisitLog(VisitLog visitLog);

    /**
     * 查看访问日志
     *
     * @param condition 条件
     * @return 访问日志列表
     */
    PageResult<VisitLog> listVisitLog(ConditionDto condition);
}
