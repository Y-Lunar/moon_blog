package com.moon.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.entity.*;
import com.moon.entity.dto.*;
import com.moon.entity.vo.*;
import com.moon.enums.FilePathEnum;
import com.moon.mapper.*;
import com.moon.service.ArticleService;
import com.moon.service.RedisService;
import com.moon.service.TagService;
import com.moon.strategy.context.SearchStrategyContext;
import com.moon.strategy.context.UploadStrategyContext;
import com.moon.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.moon.constant.CommonConstant.FALSE;
import static com.moon.constant.RedisConstant.*;
import static com.moon.enums.ArticleStatusEnum.PUBLIC;
import static com.moon.enums.FilePathEnum.ARTICLE;

/**
* @author Y.0
* @description 针对表【t_article】的数据库操作Service实现
* @createDate 2023-09-23 09:56:01
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{

    @Autowired(required = false)
    private ArticleMapper articleMapper;

    @Autowired
    private RedisService redisService;

    @Autowired(required = false)
    private CategoryMapper categoryMapper;

    @Autowired(required = false)
    private TagMapper tagMapper;

    @Autowired
    private TagService tagService;

    @Autowired(required = false)
    private ArticleTagMapper articleTagMapper;

    @Autowired(required = false)
    private UploadStrategyContext uploadStrategyContext;

    @Autowired(required = false)
    private BlogFileMapper blogFileMapper;

    @Autowired
    private SearchStrategyContext searchStrategyContext;

    /**
     * 查看博客主页文章列表
     * @return
     */
    @Override
    public PageResult<ArticleListVo> articleList() {
        // 查询文章数量
        Long count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, PUBLIC.getStatus()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询首页文章
        List<ArticleListVo> articleVoList = articleMapper.articleList(PageUtils.getLimit(), PageUtils.getSize());
        return new PageResult<>(articleVoList, count);
    }

    /**
     * 查询首页文章,根据id查询出文章分类和文章标签
     *
     * @param categoryId
     */
    private void BatchArticleList(ArticleListVo articleListVo , Integer categoryId) {
        //文章分类
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .eq(Category::getId,categoryId));
        CategoryOptionVo categoryOptionVo = BeanCopyUtils.copyBean(category,CategoryOptionVo.class);
        articleListVo.setCategory(categoryOptionVo);
        //文章标签
        List<TagOptionVo> tagOptionVos = articleTagMapper.selectTagName(articleListVo.getId());
        articleListVo.setTagVoList(tagOptionVos);
    }

    /**
     * 查看推荐文章
     * @return
     */
    @Override
    public List<ArticleRecommendVo> articleRecommendList() {
        return articleMapper.selectArticleRecommendList();
    }

    /**
     * 查看文章归档
     * @return
     */
    @Override
    public PageResult<ArchiveVo> archiveList() {
        // 查询文章数量
        Long count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, PUBLIC.getStatus()));
        if (count == 0) {
            return new PageResult<>();
        }
        List<ArchiveVo> archiveList = articleMapper.selectArchiveList(PageUtils.getLimit(), PageUtils.getSize());
        return new PageResult<>(archiveList, count);
    }

    /**
     * 查看文章
     *
     * @param articleId 文章id
     * @return
     */
    @Override
    public ArticleVo getArticleById(Integer articleId) {
        // 查询文章信息
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .eq(Article::getId,articleId)
                .eq(Article::getStatus, PUBLIC.getStatus())
                .eq(Article::getIsDelete, FALSE));
        ArticleVo articleVo = BeanCopyUtils.copyBean(article, ArticleVo.class);
        //文章分类
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .eq(Category::getId,article.getCategoryId()));
        CategoryOptionVo categoryOptionVo = BeanCopyUtils.copyBean(category,CategoryOptionVo.class);
        articleVo.setCategory(categoryOptionVo);
        //文章标签
        List<TagOptionVo> tagOptionVos = articleTagMapper.selectTagName(article.getId());
        articleVo.setTagVoList(tagOptionVos);
        if (Objects.isNull(article)) {
            return null;
        }
        // 浏览量+1
        redisService.incrZet(ARTICLE_VIEW_COUNT, articleId, 1D);
        // 查询上一篇文章
        ArticlePaginationVo lastArticle = articleMapper.selectLastArticle(articleId);
        // 查询下一篇文章
        ArticlePaginationVo nextArticle = articleMapper.selectNextArticle(articleId);
        articleVo.setLastArticle(lastArticle);
        articleVo.setNextArticle(nextArticle);
        // 查询浏览量
        Double viewCount = Optional.ofNullable(redisService.getZsetScore(ARTICLE_VIEW_COUNT, articleId))
                .orElse((double) 0);
        articleVo.setViewCount(viewCount.intValue());
        // 查询点赞量
        Integer likeCount = redisService.getHash(ARTICLE_LIKE_COUNT, articleId.toString());
        articleVo.setLikeCount(Optional.ofNullable(likeCount).orElse(0));
        return articleVo;
    }

    /**
     * 添加文章
     *
     * @param articleDto 文章信息
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addArticle(ArticleDto articleDto) {
        // 保存文章分类
        Integer categoryId = saveArticleCategory(articleDto);
        // 添加文章
        Article newArticle = BeanCopyUtils.copyBean(articleDto, Article.class);
        if (StringUtils.isBlank(newArticle.getArticleCover())) {
            SiteConfig siteConfig = redisService.getObject(SITE_SETTING);
            newArticle.setArticleCover(siteConfig.getArticleCover());
        }
        newArticle.setCategoryId(categoryId);
        newArticle.setUserId(StpUtil.getLoginIdAsInt());
        baseMapper.insert(newArticle);
        // 保存文章标签
        saveArticleTag(articleDto, newArticle.getId());
    }

    /**
     * 删除文章
     *
     * @param articleIdList 文章id集合
     * @return {@link Result <>}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteArticle(List<Integer> articleIdList) {
        // 删除文章标签
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getArticleId, articleIdList));
        // 删除文章
        articleMapper.deleteBatchIds(articleIdList);
    }

    /**
     * 回收或恢复文章
     *
     * @param deleteDto 逻辑删除
     * @return
     */
    @Override
    public void updateArticleDelete(DeleteDto deleteDto) {
        // 批量更新文章删除状态
        List<Article> articleList = deleteDto.getIdList()
                .stream()
                .map(id -> Article.builder()
                        .id(id)
                        .isDelete(deleteDto.getIsDelete())
                        .isTop(FALSE)
                        .isRecommend(FALSE)
                        .build())
                .collect(Collectors.toList());
        this.updateBatchById(articleList);
    }

    /**
     * 修改文章
     *
     * @param articleDto 文章信息
     * @return
     */
    @Override
    public void updateArticle(ArticleDto articleDto) {
        // 保存文章分类
        Integer categoryId = saveArticleCategory(articleDto);
        // 修改文章
        Article newArticle = BeanCopyUtils.copyBean(articleDto, Article.class);
        newArticle.setCategoryId(categoryId);
        newArticle.setUserId(StpUtil.getLoginIdAsInt());
        baseMapper.updateById(newArticle);
        // 保存文章标签
        saveArticleTag(articleDto, newArticle.getId());
    }

    /**
     * 编辑文章
     *
     * @param articleId 文章id
     * @return
     */
    @Override
    public ArticleInfoVo editArticle(Integer articleId) {
        // 查询文章信息
        ArticleInfoVo articleInfoVO = articleMapper.selectArticleInfoById(articleId);
        Assert.notNull(articleInfoVO, "没有该文章");
        // 查询文章分类名称
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getCategoryName)
                .eq(Category::getId, articleInfoVO.getCategoryId()));
        // 查询文章标签名称
        List<String> tagNameList = tagMapper.selectTagNameByArticleId(articleId);
        articleInfoVO.setCategoryName(category.getCategoryName());
        articleInfoVO.setTagNameList(tagNameList);
        return articleInfoVO;
    }

    /**
     * 上传文章图片
     *
     * @param file 文件
     * @return
     */
    @Override
    public String saveArticleImages(MultipartFile file) {
        // 上传文件
        String url = uploadStrategyContext.executeUploadStrategy(file, ARTICLE.getPath());
        try {
            // 获取文件md5值
            String md5 = FileUtils.getMd5(file.getInputStream());
            // 获取文件扩展名
            String extName = FileUtils.getExtension(file);
            BlogFile existFile = blogFileMapper.selectOne(new LambdaQueryWrapper<BlogFile>()
                    .select(BlogFile::getId)
                    .eq(BlogFile::getFileName, md5)
                    .eq(BlogFile::getFilePath, ARTICLE.getFilePath()));
            if (Objects.isNull(existFile)) {
                // 保存文件信息
                BlogFile newFile = BlogFile.builder()
                        .fileUrl(url)
                        .fileName(md5)
                        .filePath(ARTICLE.getFilePath())
                        .extendName(extName)
                        .fileSize((int) file.getSize())
                        .isDir(FALSE)
                        .build();
                blogFileMapper.insert(newFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 置顶文章
     *
     * @param topDto 置顶信息
     * @return
     */
    @Override
    public void updateArticleTop(TopDto topDto) {
        // 修改文章置顶状态
        Article newArticle = Article.builder()
                .id(topDto.getId())
                .isTop(topDto.getIsTop())
                .build();
        articleMapper.updateById(newArticle);
    }

    /**
     * 推荐文章
     *
     * @param recommend 推荐信息
     * @return
     */
    @Override
    public void updateArticleRecommend(RecommendDto recommend) {
        // 修改文章推荐状态
        Article newArticle = Article.builder()
                .id(recommend.getId())
                .isRecommend(recommend.getIsRecommend())
                .build();
        articleMapper.updateById(newArticle);
    }

    /**
     * 搜索文章
     *
     * @param keyword 关键字
     * @return
     */
    @Override
    public List<ArticleSearchVo> listArticlesBySearch(String keyword) {
        return searchStrategyContext.executeSearchStrategy(keyword);
    }

    /**
     * 查看后台文章列表
     *
     * @param conditionDto 条件
     * @return
     */
    @Override
    public PageResult<ArticleBackVo> listArticleBackVO(ConditionDto conditionDto) {
        // 查询文章数量
        Long count = articleMapper.countArticleBackVO(conditionDto);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询文章后台信息
        List<ArticleBackVo> articleBackVOList = articleMapper.selectArticleBackVO(PageUtils.getLimit(), PageUtils.getSize(), conditionDto);
        // 浏览量
        Map<Object, Double> viewCountMap = redisService.getZsetAllScore(ARTICLE_VIEW_COUNT);
        // 点赞量
        Map<String, Integer> likeCountMap = redisService.getHashAll(ARTICLE_LIKE_COUNT);
        // 封装文章后台信息
        articleBackVOList.forEach(item -> {
            Double viewCount = Optional.ofNullable(viewCountMap.get(item.getId())).orElse((double) 0);
            item.setViewCount(viewCount.intValue());
            Integer likeCount = likeCountMap.get(item.getId().toString());
            item.setLikeCount(Optional.ofNullable(likeCount).orElse(0));
        });
        return new PageResult<>(articleBackVOList, count);
    }

    /**
     * 保存文章标签
     *
     * @param article   文章信息
     * @param articleId 文章id
     */
    private void saveArticleTag(ArticleDto article, Integer articleId) {
        // 删除文章标签
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleId, articleId));
        // 标签名列表
        List<String> tagNameList = article.getTagNameList();
        if (CollectionUtils.isNotEmpty(tagNameList)) {
            // 查询出已存在的标签
            List<Tag> existTagList = tagMapper.selectTagListRe(tagNameList);
            List<String> existTagNameList = existTagList.stream()
                    .map(Tag::getTagName)
                    .collect(Collectors.toList());
            List<Integer> existTagIdList = existTagList.stream()
                    .map(Tag::getId)
                    .collect(Collectors.toList());
            // 移除已存在的标签列表
            tagNameList.removeAll(existTagNameList);
            // 含有新标签
            if (CollectionUtils.isNotEmpty(tagNameList)) {
                // 新标签列表
                List<Tag> newTagList = tagNameList.stream()
                        .map(item -> Tag.builder()
                                .tagName(item)
                                .build())
                        .collect(Collectors.toList());
                // 批量保存新标签
                tagService.saveBatch(newTagList);
                // 获取新标签id列表
                List<Integer> newTagIdList = newTagList.stream()
                        .map(Tag::getId)
                        .collect(Collectors.toList());
                // 新标签id添加到id列表
                existTagIdList.addAll(newTagIdList);
            }
            // 将所有的标签绑定到文章标签关联表
            articleTagMapper.saveBatchArticleTag(articleId, existTagIdList);
        }
    }

    /**
     * 保存文章分类
     *
     * @param article 文章信息
     * @return 文章分类
     */
    private Integer saveArticleCategory(ArticleDto article) {
        // 查询分类
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getId)
                .eq(Category::getCategoryName, article.getCategoryName()));
        // 分类不存在
        if (Objects.isNull(category)) {
            category = Category.builder()
                    .categoryName(article.getCategoryName())
                    .build();
            // 保存分类
            categoryMapper.insert(category);
        }
        return category.getId();
    }
}




