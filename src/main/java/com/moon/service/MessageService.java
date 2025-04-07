package com.moon.service;

import com.moon.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.entity.dto.CheckDto;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.MessageDto;
import com.moon.entity.vo.MessageBackVO;
import com.moon.entity.vo.MessageVo;
import com.moon.entity.vo.PageResult;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_message】的数据库操作Service
* @createDate 2023-10-13 17:08:35
*/
public interface MessageService extends IService<Message> {

    /**
     * 查看留言列表
     *
     * @return
     */
    List<MessageVo> messageList();

    /**
     * 添加留言
     *
     * @param messageDto 留言信息
     * @return
     */
    void insertMessage(MessageDto messageDto);

    /**
     * 查看后台留言列表
     *
     * @param condition 条件
     * @return 后台留言列表
     */
    PageResult<MessageBackVO> messageListVo(ConditionDto condition);

    /**
     * 审核留言
     *
     * @param check 审核信息
     * @return
     */
    void updateMessageCheck(CheckDto check);
}
