package com.moon.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_operation_log
 */
@TableName(value ="t_operation_log")
@Data
public class OperationLog implements Serializable {
    /**
     * 操作id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 操作模块
     */
    private String module;

    /**
     * 操作类型
     */
    private String type;

    /**
     * 操作uri
     */
    private String uri;

    /**
     * 方法名称
     */
    private String name;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 返回数据
     */
    private String data;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 操作ip
     */
    private String ipAddress;

    /**
     * 操作地址
     */
    private String ipSource;

    /**
     * 操作耗时 (毫秒)
     */
    private Long times;

    /**
     * 操作时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}