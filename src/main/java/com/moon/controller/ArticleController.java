package com.moon.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.moon.annotation.AccessLimit;
import com.moon.annotation.OptLogger;
import com.moon.annotation.VisitLogger;
import com.moon.entity.dto.*;
import com.moon.entity.vo.*;
import com.moon.enums.LikeTypeEnum;
import com.moon.service.ArticleService;
import com.moon.service.CategoryService;
import com.moon.strategy.context.LikeStrategyContext;
import com.moon.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.moon.constant.OptTypeConstant.*;

/**
 * 文章管理模块
 *
 * @author:Y.0
 * @date:2023/10/8
 */

@Api(tags = "文章模块")
@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private LikeStrategyContext likeStrategyContext;

    /**
     * 查看博客主页文章列表
     *
     * @return
     */
    @VisitLogger(value = "首页")
    @ApiOperation("查看博客主页文章列表")
    @GetMapping("/article/list")
    public Result<PageResult<ArticleListVo>> articleList(){
        PageResult<ArticleListVo> articleListVoPageResult = articleService.articleList();
        return Result.success(articleListVoPageResult);
    }

    /**
     * 查看推荐文章
     *
     * @return
     */
    @ApiOperation(value = "查看推荐文章")
    @GetMapping("/article/recommend")
    public Result<List<ArticleRecommendVo>> articleRecommendList() {
        List<ArticleRecommendVo> articleRecommendVoList = articleService.articleRecommendList();
        return Result.success(articleRecommendVoList);
    }

    /**
     * 查看文章归档
     *
     * @return
     */
    @VisitLogger(value = "归档")
    @ApiOperation(value = "查看文章归档")
    @GetMapping("/archives/list")
    public Result<PageResult<ArchiveVo>> archiveList() {
        PageResult<ArchiveVo> archiveVoPageResult = articleService.archiveList();
        return Result.success(archiveVoPageResult);
    }

    /**
     * 查看文章
     *
     * @param articleId 文章id
     * @return
     */
    @VisitLogger(value = "文章")
    @ApiOperation(value = "查看文章")
    @GetMapping("/article/{articleId}")
    public Result<ArticleVo> getArticleById(@PathVariable("articleId") Integer articleId) {
        return Result.success(articleService.getArticleById(articleId));
    }

    /**
     * 添加文章
     *
     * @param articleDto 文章信息
     * @return
     */
    @OptLogger(value = ADD)
    @ApiOperation(value = "添加文章")
    @SaCheckPermission("blog:article:add")
    @PostMapping("/admin/article/add")
    public Result<?> addArticle(@Validated @RequestBody ArticleDto articleDto) {
        articleService.addArticle(articleDto);
        return Result.success();
    }

    /**
     * 删除文章
     *
     * @param articleIdList 文章id集合
     * @return {@link Result<>}
     */
    @OptLogger(value = DELETE)
    @ApiOperation(value = "删除文章")
    @SaCheckPermission("blog:article:delete")
    @DeleteMapping("/admin/article/delete")
    public Result<?> deleteArticle(@RequestBody List<Integer> articleIdList) {
        articleService.deleteArticle(articleIdList);
        return Result.success();
    }

    /**
     * 回收或恢复文章
     *
     * @param deleteDto 逻辑删除
     * @return
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "回收或恢复文章")
    @SaCheckPermission("blog:article:recycle")
    @PutMapping("/admin/article/recycle")
    public Result<?> updateArticleDelete(@Validated @RequestBody DeleteDto deleteDto) {
        articleService.updateArticleDelete(deleteDto);
        return Result.success();
    }

    /**
     * 修改文章
     *
     * @param articleDto 文章信息
     * @return
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "修改文章")
    @SaCheckPermission("blog:article:update")
    @PutMapping("/admin/article/update")
    public Result<?> updateArticle(@Validated @RequestBody ArticleDto articleDto) {
        articleService.updateArticle(articleDto);
        return Result.success();
    }

    /**
     * 编辑文章
     *
     * @param articleId 文章id
     * @return
     */
    @ApiOperation(value = "编辑文章")
    @SaCheckPermission("blog:article:edit")
    @GetMapping("/admin/article/edit/{articleId}")
    public Result<ArticleInfoVo> editArticle(@PathVariable("articleId") Integer articleId) {
        return Result.success(articleService.editArticle(articleId));
    }

    /**
     * 上传文章图片
     *
     * @param file 文件
     * @return
     */
    @OptLogger(value = UPLOAD)
    @ApiOperation(value = "上传文章图片")
    @ApiImplicitParam(name = "file", value = "文章图片", required = true, dataType = "MultipartFile")
    @SaCheckPermission("blog:article:upload")
    @PostMapping("/admin/article/upload")
    public Result<String> saveArticleImages(@RequestParam("file") MultipartFile file) {
        return Result.success(articleService.saveArticleImages(file));
    }

    /**
     * 置顶文章
     *
     * @param topDto 置顶信息
     * @return
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "置顶文章")
    @SaCheckPermission("blog:article:top")
    @PutMapping("/admin/article/top")
    public Result<?> updateArticleTop(@Validated @RequestBody TopDto topDto) {
        articleService.updateArticleTop(topDto);
        return Result.success();
    }

    /**
     * 推荐文章
     *
     * @param recommend 推荐信息
     * @return
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "推荐文章")
    @SaCheckPermission("blog:article:recommend")
    @PutMapping("/admin/article/recommend")
    public Result<?> updateArticleRecommend(@Validated @RequestBody RecommendDto recommend) {
        articleService.updateArticleRecommend(recommend);
        return Result.success();
    }

    /**
     * 搜索文章
     *
     * @param keyword 关键字
     * @return
     */
    @ApiOperation(value = "搜索文章")
    @GetMapping("/article/search")
    public Result<List<ArticleSearchVo>> listArticlesBySearch(String keyword) {
        return Result.success(articleService.listArticlesBySearch(keyword));
    }

    /**
     * 查看后台文章列表
     *
     * @param conditionDto 条件
     * @return
     */
    @ApiOperation(value = "查看后台文章列表")
    @SaCheckPermission("blog:article:list")
    @GetMapping("/admin/article/list")
    public Result<PageResult<ArticleBackVo>> listArticleBackVO(ConditionDto conditionDto) {
        PageResult<ArticleBackVo> articleBackVoPageResult = articleService.listArticleBackVO(conditionDto);
        return Result.success(articleBackVoPageResult);
    }

    /**
     * 点赞文章
     *
     * @param articleId 文章id
     * @return {@link Result<>}
     */
    @SaCheckLogin
    @ApiOperation(value = "点赞文章")
    @AccessLimit(seconds = 60, maxCount = 3)
    @SaCheckPermission("blog:article:like")
    @PostMapping("/article/{articleId}/like")
    public Result<?> likeArticle(@PathVariable("articleId") Integer articleId) {
        likeStrategyContext.executeLikeStrategy(LikeTypeEnum.ARTICLE, articleId);
        return Result.success();
    }
}
