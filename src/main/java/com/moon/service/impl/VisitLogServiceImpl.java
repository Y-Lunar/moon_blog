package com.moon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.entity.VisitLog;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.vo.PageResult;
import com.moon.service.VisitLogService;
import com.moon.mapper.VisitLogMapper;
import com.moon.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
* @author Y.0
* @description 针对表【t_visit_log】的数据库操作Service实现
* @createDate 2023-10-24 18:45:03
 *
*/
@Service
public class VisitLogServiceImpl extends ServiceImpl<VisitLogMapper, VisitLog>
    implements VisitLogService{

    @Autowired(required = false)
    private VisitLogMapper visitLogMapper;

    @Override
    public void saveVisitLog(VisitLog visitLog) {
        // 保存访问日志
        visitLogMapper.insert(visitLog);
    }

    @Override
    public PageResult<VisitLog> listVisitLog(ConditionDto condition) {
        // 查询访问日志数量
        Long count = visitLogMapper.selectCount(new LambdaQueryWrapper<VisitLog>()
                .like(StringUtils.hasText(condition.getKeyword()), VisitLog::getPage, condition.getKeyword())
        );
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询访问日志列表
        List<VisitLog> visitLogVOList = visitLogMapper.selectVisitLogList(PageUtils.getLimit(),
                PageUtils.getSize(), condition.getKeyword());
        return new PageResult<>(visitLogVOList, count);
    }
}




