package com.moon.service;

import com.moon.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.entity.dto.CheckDto;
import com.moon.entity.dto.CommentDto;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.vo.*;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_comment】的数据库操作Service
* @createDate 2023-10-12 10:41:59
*/
public interface CommentService extends IService<Comment> {

    /**
     * 查看最新评论（top 5）
     *
     * @return
     */
    List<RecentCommentVo> recentCommentList();

    /**
     * 查看回复评论
     *
     * @param commentId 评论id
     * @return
     */
    List<RelyVo> listResultById(Integer commentId);

    /**
     * 查看评论
     *
     * @param condition
     * @return
     */
    PageResult<CommentVo> CommentVoList(ConditionDto condition);

    /**
     * 查看后台评论列表
     *
     * @param condition 条件
     * @return 后台评论
     */
    PageResult<CommentBackVo> commentListVo(ConditionDto condition);

    /**
     * 添加评论
     *
     * @param comment 评论信息
     * @return
     */
    void addComment(CommentDto comment);

    /**
     * 审核评论
     *
     * @param check 审核信息
     * @return
     */
    void updateCommentCheck(CheckDto check);
}
