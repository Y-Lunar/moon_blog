package com.moon.service;

import com.moon.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.entity.dto.*;
import com.moon.entity.vo.*;
import com.moon.utils.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_article】的数据库操作Service
* @createDate 2023-09-23 09:56:01
*/
public interface ArticleService extends IService<Article> {

    /**
     * 查看博客主页文章列表
     * @return
     */
    PageResult<ArticleListVo> articleList();

    /**
     * 查看推荐文章
     * @return
     */
    List<ArticleRecommendVo> articleRecommendList();

    /**
     * 查看文章归档
     * @return
     */
    PageResult<ArchiveVo> archiveList();

    /**
     * 查看文章
     *
     * @param articleId 文章id
     * @return
     */
    ArticleVo getArticleById(Integer articleId);

    /**
     * 添加文章
     *
     * @param articleDto 文章信息
     * @return
     */
    void addArticle(ArticleDto articleDto);

    /**
     * 删除文章
     *
     * @param articleIdList 文章id集合
     * @return {@link Result <>}
     */
    void deleteArticle(List<Integer> articleIdList);

    /**
     * 回收或恢复文章
     *
     * @param deleteDto 逻辑删除
     * @return
     */
    void updateArticleDelete(DeleteDto deleteDto);

    /**
     * 修改文章
     *
     * @param articleDto 文章信息
     * @return
     */
    void updateArticle(ArticleDto articleDto);

    /**
     * 编辑文章
     *
     * @param articleId 文章id
     * @return
     */
    ArticleInfoVo editArticle(Integer articleId);

    /**
     * 上传文章图片
     *
     * @param file 文件
     * @return
     */
    String saveArticleImages(MultipartFile file);

    /**
     * 置顶文章
     *
     * @param topDto 置顶信息
     * @return
     */
    void updateArticleTop(TopDto topDto);

    /**
     * 推荐文章
     *
     * @param recommend 推荐信息
     * @return
     */
    void updateArticleRecommend(RecommendDto recommend);

    /**
     * 搜索文章
     *
     * @param keyword 关键字
     * @return
     */
    List<ArticleSearchVo> listArticlesBySearch(String keyword);

    /**
     * 查看后台文章列表
     *
     * @param conditionDto 条件
     * @return
     */
    PageResult<ArticleBackVo> listArticleBackVO(ConditionDto conditionDto);
}
