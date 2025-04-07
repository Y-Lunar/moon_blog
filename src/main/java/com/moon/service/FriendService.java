package com.moon.service;

import com.moon.entity.Friend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.FriendDto;
import com.moon.entity.vo.FriendBackVo;
import com.moon.entity.vo.FriendVO;
import com.moon.entity.vo.PageResult;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_friend】的数据库操作Service
* @createDate 2023-10-13 17:23:02
*/
public interface FriendService extends IService<Friend> {

    /**
     * 查看友链列表
     *
     * @return
     */
    List<FriendVO> FriendVoList();

    /**
     * 查看友链后台列表
     *
     * @param condition 查询条件
     * @return
     */
    PageResult<FriendBackVo> friendListVo(ConditionDto condition);

    /**
     * 添加友链
     *
     * @param friend 友链
     * @return
     */
    void addFriend(FriendDto friend);

    /**
     * 修改友链
     *
     * @param friend 友链
     * @return
     */
    void updateFriend(FriendDto friend);
}
