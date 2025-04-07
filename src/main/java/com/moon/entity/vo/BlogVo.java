package com.moon.entity.vo;

import com.moon.entity.SiteConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 博客信息
 *
 * @author:Y.0
 * @date:2023/9/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogVo implements Serializable {

    /**
     * 文章数量
     */
    @ApiModelProperty(value = "文章数量")
    private Long articleCount;

    /**
     * 分类数量
     */
    @ApiModelProperty(value = "分类数量")
    private Long categoryCount;

    /**
     * 标签数量
     */
    @ApiModelProperty(value = "标签数量")
    private Long tagCount;

    /**
     * 网站访问量
     */
    @ApiModelProperty(value = "网站访问量")
    private String viewCount;

    /**
     * 网站配置
     */
    @ApiModelProperty(value = "网站配置")
    private SiteConfig siteConfig;
}
