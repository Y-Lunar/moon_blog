package com.moon.mapper;

import com.moon.entity.Friend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moon.entity.vo.FriendBackVo;
import com.moon.entity.vo.FriendVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_friend】的数据库操作Mapper
* @createDate 2023-10-13 17:23:02
* @Entity com.moon.entity.Friend
*/
public interface FriendMapper extends BaseMapper<Friend> {

    /**
     * 查看友链列表
     *
     * @return 友链列表
     */
    List<FriendVO> SelectFriendVoList();

    /**
     * 查看友链后台列表
     *
     * @param limit   页码
     * @param size    大小
     * @param keyword 条件
     * @return 友链后台列表
     */
    List<FriendBackVo> selectFriendBackVOList(@Param("limit") Long limit, @Param("size") Long size, @Param("keyword") String keyword);
}




