package com.moon.mapper;

import com.moon.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.vo.MessageBackVO;
import com.moon.entity.vo.MessageVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_message】的数据库操作Mapper
* @createDate 2023-10-13 17:08:35
* @Entity com.moon.entity.Message
*/
public interface MessageMapper extends BaseMapper<Message> {

    /**
     * 查看留言列表
     * @return
     */
    List<MessageVo> selectMessageList();

    /**
     * 查询后台留言列表
     *
     * @param limit     页码
     * @param size      大小
     * @param condition 条件
     * @return 后台留言列表
     */
    List<MessageBackVO> selectMessageBackVOList(@Param("limit") Long limit, @Param("size") Long size, @Param("condition") ConditionDto condition);

}




