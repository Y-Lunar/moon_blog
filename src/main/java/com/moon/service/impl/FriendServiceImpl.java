package com.moon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.entity.Friend;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.FriendDto;
import com.moon.entity.vo.FriendBackVo;
import com.moon.entity.vo.FriendVO;
import com.moon.entity.vo.PageResult;
import com.moon.service.FriendService;
import com.moon.mapper.FriendMapper;
import com.moon.utils.BeanCopyUtils;
import com.moon.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_friend】的数据库操作Service实现
* @createDate 2023-10-13 17:23:02
*/
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend>
    implements FriendService{

    @Autowired(required = false)
    private FriendMapper friendMapper;

    /**
     * 查看友链列表
     *
     * @return
     */
    @Override
    public List<FriendVO> FriendVoList() {
        // 查询友链列表
        return friendMapper.SelectFriendVoList();
    }

    /**
     * 查看友链后台列表
     *
     * @param condition 查询条件
     * @return
     */
    @Override
    public PageResult<FriendBackVo> friendListVo(ConditionDto condition) {
        // 查询友链数量
        Long count = friendMapper.selectCount(new LambdaQueryWrapper<Friend>()
                .like(StringUtils.hasText(condition.getKeyword()), Friend::getName, condition.getKeyword())
        );
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台友链列表
        List<FriendBackVo> friendBackVOList = friendMapper.selectFriendBackVOList(PageUtils.getLimit(), PageUtils.getSize(), condition.getKeyword());
        return new PageResult<>(friendBackVOList, count);
    }

    /**
     * 添加友链
     *
     * @param friend 友链
     * @return
     */
    @Override
    public void addFriend(FriendDto friend) {
        // 新友链
        Friend newFriend = BeanCopyUtils.copyBean(friend, Friend.class);
        // 添加友链
        baseMapper.insert(newFriend);
    }

    /**
     * 修改友链
     *
     * @param friend 友链
     * @return
     */
    @Override
    public void updateFriend(FriendDto friend) {
        // 新友链
        Friend newFriend = BeanCopyUtils.copyBean(friend, Friend.class);
        // 更新友链
        baseMapper.updateById(newFriend);
    }
}




