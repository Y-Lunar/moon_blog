package com.moon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.entity.Message;
import com.moon.entity.SiteConfig;
import com.moon.entity.dto.CheckDto;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.MessageDto;
import com.moon.entity.vo.MessageBackVO;
import com.moon.entity.vo.MessageVo;
import com.moon.entity.vo.PageResult;
import com.moon.service.MessageService;
import com.moon.mapper.MessageMapper;
import com.moon.service.SiteConfigService;
import com.moon.utils.BeanCopyUtils;
import com.moon.utils.HTMLUtils;
import com.moon.utils.IpUtils;
import com.moon.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.moon.constant.CommonConstant.FALSE;
import static com.moon.constant.CommonConstant.TRUE;

/**
* @author Y.0
* @description 针对表【t_message】的数据库操作Service实现
* @createDate 2023-10-13 17:08:35
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

    @Autowired(required = false)
    private MessageMapper messageMapper;

    @Autowired
    private SiteConfigService siteConfigService;

    @Autowired
    private HttpServletRequest  request;

    /**
     * 查看留言列表
     *
     * @return
     */
    @Override
    public List<MessageVo> messageList() {
        List<MessageVo> messageVoList = messageMapper.selectMessageList();
        return messageVoList;
    }

    /**
     * 添加留言
     *
     * @param messageDto 留言信息
     * @return
     */
    @Override
    public void insertMessage(MessageDto messageDto) {
        SiteConfig siteConfig = siteConfigService.getSiteConfig();
        Integer messageCheck = siteConfig.getMessageCheck();
        String ipAddress = IpUtils.getIpAddress(request);
        String ipSource = IpUtils.getIpSource(ipAddress);
        Message newMessage = BeanCopyUtils.copyBean(messageDto, Message.class);
        newMessage.setMessageContent(HTMLUtils.filter(messageDto.getMessageContent()));
        newMessage.setIpAddress(ipAddress);
        newMessage.setIsCheck(messageCheck.equals(FALSE) ? TRUE : FALSE);
        newMessage.setIpSource(ipSource);
        messageMapper.insert(newMessage);
    }

    /**
     * 查看后台留言列表
     *
     * @param condition 条件
     * @return 后台留言列表
     */
    @Override
    public PageResult<MessageBackVO> messageListVo(ConditionDto condition) {
        // 查询留言数量
        Long count = messageMapper.selectCount(new LambdaQueryWrapper<Message>()
                .like(StringUtils.hasText(condition.getKeyword()), Message::getNickname, condition.getKeyword())
                .eq(Objects.nonNull(condition.getIsCheck()), Message::getIsCheck, condition.getIsCheck()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台友链列表
        List<MessageBackVO> messageBackVOList = messageMapper.selectMessageBackVOList(PageUtils.getLimit(), PageUtils.getSize(), condition);
        return new PageResult<>(messageBackVOList, count);
    }

    /**
     * 审核留言
     *
     * @param check 审核信息
     * @return
     */
    @Override
    public void updateMessageCheck(CheckDto check) {
        // 修改留言审核状态
        List<Message> messageList = check.getIdList()
                .stream()
                .map(id -> Message.builder()
                        .id(id)
                        .isCheck(check.getIsCheck())
                        .build())
                .collect(Collectors.toList());
        this.updateBatchById(messageList);
    }


}




