package com.moon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务状态枚举
 * @author:Y.0
 * @date:2023/8/29
 */
@Getter
@AllArgsConstructor
public enum TaskStatusEnum {

    /**
     * 运行
     */
    RUNNING(1),

    /**
     * 暂停
     */
    PAUSE(0);

    /**
     * 状态
     */
    private final Integer status;
}
