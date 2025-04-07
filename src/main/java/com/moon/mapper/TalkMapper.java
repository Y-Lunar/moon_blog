package com.moon.mapper;

import com.moon.entity.Talk;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moon.entity.vo.TalkBackInfoVo;
import com.moon.entity.vo.TalkBackVo;
import com.moon.entity.vo.TalkVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_talk】的数据库操作Mapper
* @createDate 2023-10-08 16:19:12
* @Entity com.moon.entity.Talk
*/
public interface TalkMapper extends BaseMapper<Talk> {

    /**
     * 查询说说列表
     *
     * @param limit 页码
     * @param size 大小
     * @return
     */
    List<TalkVo> selectTalkList(@Param("limit") Long limit, @Param("size") Long size);

    /**
     * 根据id查询说说
     *
     * @param talkId
     * @return
     */
    TalkVo selectTalkById(Integer talkId);

    /**
     * 查询后台说说列表
     *
     * @param limit  页码
     * @param size   大小
     * @param status 状态
     * @return 后台说说列表
     */
    List<TalkBackVo> selectTalkBackVo(@Param("limit") Long limit, @Param("size") Long size, @Param("status") Integer status);

    /**
     * 根据id查询后台说说
     *
     * @param talkId 说说id
     * @return 后台说说
     */
    TalkBackInfoVo selectTalkBackById(Integer talkId);
}




