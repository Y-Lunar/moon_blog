package com.moon.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 标签Dto
 *
 * @author:Y.0
 * @date:2023/10/15
 */
@Data
@ApiModel(description = "标签Dto")
public class TagDto {
    /**
     * 标签id
     */
    @ApiModelProperty(value = "标签id")
    private Integer id;

    /**
     * 标签名
     */
    @NotBlank(message = "标签名不能为空")
    @ApiModelProperty(value = "标签名")
    private String tagName;
}
