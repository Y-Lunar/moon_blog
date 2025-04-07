package com.moon.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import static com.moon.enums.StatusCodeEnum.FAIL;
import static com.moon.enums.StatusCodeEnum.SUCCESS;

/**
 * 结果返回类
 *
 * @author:Y.0
 * @date:2023/9/21
 */
@Data
@ApiModel(description = "结果返回类")
public class Result<T> {


    /**
     * 返回状态
     */
    @ApiModelProperty(value = "返回状态")
    private Boolean flag;

    /**
     * 状态码
     */
    @ApiModelProperty(value = "状态码")
    private Integer code;

    /**
     * 返回信息
     */
    @ApiModelProperty(value = "返回信息")
    private String msg;

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private T data;

    public static <T> Result<T> success() {
        return buildResult(true, null, SUCCESS.getCode(), SUCCESS.getMsg());
    }

    public static <T> Result<T> success(T data) {
        return buildResult(true, data, SUCCESS.getCode(), SUCCESS.getMsg());
    }

    public static <T> Result<T> error(String message) {
        return buildResult(false, null, FAIL.getCode(), message);
    }

    public static <T> Result<T> error(Integer code, String message) {
        return buildResult(false, null, code, message);
    }

    private static <T> Result<T> buildResult(Boolean flag, T data, Integer code, String message) {
        Result<T> r = new Result<>();
        r.setFlag(flag);
        r.setData(data);
        r.setCode(code);
        r.setMsg(message);
        return r;
    }

}