package com.moon.strategy;

import com.moon.entity.vo.ArticleSearchVo;

import java.util.List;

/**
 * @author:Y.0
 * @date:2023/10/24
 */
public interface SearchStrategy {

    /**
     * 搜索文章
     *
     * @param keyword 关键字
     * @return {@link List <ArticleSearchVo>} 文章列表
     */
    List<ArticleSearchVo> searchArticle(String keyword);

}
