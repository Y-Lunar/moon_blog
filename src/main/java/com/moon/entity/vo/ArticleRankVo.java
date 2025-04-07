package com.moon.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 文章浏览量排行
 *
 * @author:Y.0
 * @date:2023/10/13
 */

@Data
@Builder
@ApiModel(description = "文章浏览量排行")
public class ArticleRankVo {

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String articleTitle;

    /**
     * 浏览量
     */
    @ApiModelProperty(value = "浏览量")
    private Integer viewCount;

}
