package com.moon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 时区枚举
 * @author:Y.0
 * @date:2023/8/29
 */

@Getter
@AllArgsConstructor
public enum ZoneEnum {

    /**
     * 上海
     */
    SHANGHAI("Asia/Shanghai", "中国上海");

    /**
     * 时区
     */
    private final String zone;

    /**
     * 描述
     */
    private final String description;

}
