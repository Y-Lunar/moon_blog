package com.moon.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文章贡献统计
 *
 * @author:Y.0
 * @date:2023/10/13
 */

@Data
@ApiModel(description = "文章贡献统计")
public class ArticleStatisticsVo {
    /**
     * 日期
     */
    @ApiModelProperty(value = "日期")
    private String date;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Integer count;
}
