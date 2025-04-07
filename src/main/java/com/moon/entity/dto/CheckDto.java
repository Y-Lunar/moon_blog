package com.moon.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 审核Dto
 *
 * @author:Y.0
 * @date:2023/10/23
 */
@Data
@ApiModel(description = "审核Dto")
public class CheckDto {

    /**
     * id集合
     */
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id集合")
    private List<Integer> idList;

    /**
     * 是否通过 (0否 1是)
     */
    @NotNull(message = "状态值不能为空")
    @ApiModelProperty(value = "是否通过 (0否 1是)")
    private Integer isCheck;

}
