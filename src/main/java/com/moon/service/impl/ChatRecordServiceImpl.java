package com.moon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.entity.ChatRecord;
import com.moon.service.ChatRecordService;
import com.moon.mapper.ChatRecordMapper;
import org.springframework.stereotype.Service;

/**
* @author Y.0
* @description 针对表【t_chat_record】的数据库操作Service实现
* @createDate 2023-10-24 18:45:36
*/
@Service
public class ChatRecordServiceImpl extends ServiceImpl<ChatRecordMapper, ChatRecord>
    implements ChatRecordService{

}




