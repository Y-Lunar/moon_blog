package com.moon.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName t_talk
 */
@TableName(value ="t_talk")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Talk implements Serializable {
    /**
     * 说说id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 说说内容
     */
    private String talkContent;

    /**
     * 说说图片
     */
    private String images;

    /**
     * 是否置顶 (0否 1是)
     */
    private Integer isTop;

    /**
     * 状态 (1公开  2私密)
     */
    private Integer status;

    /**
     * 发表时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}