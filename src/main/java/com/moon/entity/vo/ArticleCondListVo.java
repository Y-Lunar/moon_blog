package com.moon.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 文章条件列表Vo
 *
 * @author:Y.0
 * @date:2023/10/13
 */
@Data
@Builder
@ApiModel(description = "文章条件列表Vo")
public class ArticleCondListVo {

    /**
     * 文章列表
     */
    @ApiModelProperty(value = "文章列表")
    private List<ArticleCondition> articleConditionVOList;

    /**
     * 条件名
     */
    @ApiModelProperty(value = "条件名")
    private String name;

}
