package com.moon.mapper;

import com.moon.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_article】的数据库操作Mapper
* @createDate 2023-09-23 09:56:01
* @Entity com.moon.entity.Article
*/
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 查询首页文章
     *
     * @param limit
     * @param size
     * @return
     */
    List<ArticleListVo> articleList(@Param("limit") Long limit, @Param("size") Long size);

    /**
     * 查询推荐文章
     * @return
     */
    List<ArticleRecommendVo> selectArticleRecommendList();

    /**
     * 查询文章归档
     * @param limit
     * @param size
     * @return
     */
    List<ArchiveVo> selectArchiveList(Long limit, Long size);

    /**
     * 根据id查询首页文章
     *
     * @param articleId 文章id
     * @return 首页文章
     */
    ArticleVo getArticleById(Integer articleId);

    /**
     * 查询上一篇文章
     *
     * @param articleId 文章id
     * @return 上一篇文章
     */
    ArticlePaginationVo selectLastArticle(Integer articleId);

    /**
     * 查询下一篇文章
     *
     * @param articleId 文章id
     * @return 下一篇文章
     */
    ArticlePaginationVo selectNextArticle(Integer articleId);

    /**
     * 查询文章统计
     *
     * @return 文章统计
     */
    List<ArticleStatisticsVo> selectArticleStatistics();


    /**
     * 根据条件查询文章
     *
     * @param limit     页码
     * @param size      大小
     * @param condition 条件
     * @return 文章列表
     */
    List<ArticleCondition> listArticleByCondition(@Param("limit") Long limit, @Param("size") Long size, @Param("condition") ConditionDto condition);

    /**
     * 根据id查询文章信息
     *
     * @param articleId 文章id
     * @return 文章信息
     */
    ArticleInfoVo selectArticleInfoById(@Param("articleId") Integer articleId);

    /**
     * 查询后台文章数量
     *
     * @param condition 条件
     * @return 文章数量
     */
    Long countArticleBackVO(@Param("condition") ConditionDto condition);

    /**
     * 查询后台文章列表
     *
     * @param limit     页码
     * @param size      大小
     * @param condition 条件
     * @return 后台文章列表
     */
    List<ArticleBackVo> selectArticleBackVO(@Param("limit") Long limit, @Param("size") Long size, @Param("condition") ConditionDto condition);

    /**
     * 文章搜索
     *
     * @param keyword 关键字
     * @return 文章列表
     */
    List<ArticleSearchVo> searchArticle(@Param("keyword") String keyword);
}




