package com.moon.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户角色Vo
 *
 * @author:Y.0
 * @date:2023/10/23
 */
@Data
@ApiModel(description = "用户角色Vo")
public class UserRoleVo {

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id")
    private String id;

    /**
     * 角色名
     */
    @ApiModelProperty(value = "角色名")
    private String roleName;

}
