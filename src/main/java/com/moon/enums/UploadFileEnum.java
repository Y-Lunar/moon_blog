package com.moon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 上传文件异常信息枚举
 * @author:Y.0
 * @date:2023/9/2
 */
@Getter
@AllArgsConstructor
public enum UploadFileEnum {

    UPLOAD_FILE_LARGE("上传文件太大不能超过5MB","5MB");

    private final String large;

    private final String mb;
}
