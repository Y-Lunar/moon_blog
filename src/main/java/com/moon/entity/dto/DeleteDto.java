package com.moon.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 逻辑删除
 *
 * @author:Y.0
 * @date:2023/10/15
 */
@Data
@ApiModel(description = "逻辑删除")
@Builder
public class DeleteDto {

    /**
     * id列表
     */
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id列表")
    private List<Integer> idList;

    /**
     * 是否删除 (0否 1是)
     */
    @NotNull(message = "状态值不能为空")
    @ApiModelProperty(value = "是否删除 (0否 1是)")
    private Integer isDelete;

}
