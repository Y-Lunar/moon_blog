package com.moon.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页返回类
 *
 * @author:Y.0
 * @date:2023/10/8
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "分页返回类")
public class PageResult<T> {

    /**
     *
     */
    @ApiModelProperty("分页结果")
    private List<T> resultList;

    /**
     * 总数
     */
    @ApiModelProperty("总数")
    private Long total;
}
