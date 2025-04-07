package com.moon.mapper;

import com.moon.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_comment】的数据库操作Mapper
* @createDate 2023-10-12 10:41:59
* @Entity com.moon.entity.Comment
*/
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 查看最新评论
     *
     * @return
     */
    List<RecentCommentVo> selectRecentCommList();

    /**
     * 查询子评论（分页查询）
     * @param limit   页码
     * @param size    大小
     * @param commentId id
     * @return
     */
    List<RelyVo> selectReplyByPid(@Param("limit") Long limit, @Param("size") Long size, @Param("commentId") Integer commentId);

    /**
     * 分页查询父评论
     *
     * @param limit     分页
     * @param size      大小
     * @param condition 条件
     * @return 评论集合
     */
    List<CommentVo> selectParentComment(@Param("limit") Long limit, @Param("size") Long size, @Param("condition") ConditionDto condition);

    /**
     * 查询每条父评论下的前三条子评论
     *
     * @param parentCommentIdList 父评论id集合
     * @return 回复集合
     */
    List<RelyVo> selectReplyByParentIdList(@Param("parentCommentIdList") List<Integer> parentCommentIdList);

    /**
     * 根据父评论id查询回复数量
     *
     * @param parentCommentIdList 父评论id列表
     * @return 回复数量
     */
    List<ReplyCountVo> selectReplyCountByParentId(@Param("parentCommentIdList") List<Integer> parentCommentIdList);

    /**
     * 根据评论类型id获取评论量
     *
     * @param typeIdList  类型id列表
     * @param commentType 评论类型
     * @return
     */
    List<CommentCountVo> selectCommentCountByTypeId(@Param("typeIdList") List<Integer> typeIdList, @Param("commentType") Integer commentType);

    /**
     * 查询评论数量
     *
     * @param condition 条件
     * @return 评论数量
     */
    Long countComment(@Param("condition") ConditionDto condition);

    /**
     * 查询后台评论列表
     *
     * @param limit     分页
     * @param size      大小
     * @param condition 条件
     * @return 评论集合
     */
    List<CommentBackVo> commentListVo(@Param("limit") Long limit, @Param("size") Long size, @Param("condition") ConditionDto condition);


    /**
     * 根据父评论id查询子评论id
     *
     * @param parentId 父评论id
     * @return 子评论id列表
     */
    List<Integer> selectCommentIdByParentId(@Param("parentId") Integer parentId);
}




