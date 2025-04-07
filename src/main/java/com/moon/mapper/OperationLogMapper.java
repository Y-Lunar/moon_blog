package com.moon.mapper;

import com.moon.entity.OperationLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.vo.OperationLogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_operation_log】的数据库操作Mapper
* @createDate 2023-10-24 18:45:21
* @Entity com.moon.entity.OperationLog
*/
public interface OperationLogMapper extends BaseMapper<OperationLog> {


    /**
     * 查询操作日志
     *
     * @param limit     页码
     * @param size      大小
     * @param condition 条件
     * @return 操作日志列表
     */
    List<OperationLogVO> selectOperationLogVOList(@Param("limit") Long limit, @Param("size") Long size, @Param("condition") ConditionDto condition);

}




