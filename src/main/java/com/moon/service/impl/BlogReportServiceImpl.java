package com.moon.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.moon.entity.Article;
import com.moon.entity.SiteConfig;
import com.moon.entity.vo.*;
import com.moon.mapper.*;
import com.moon.service.BlogReportService;
import com.moon.service.RedisService;
import com.moon.service.SiteConfigService;
import com.moon.utils.UserAgentUtils;
import com.moon.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.moon.constant.CommonConstant.FALSE;
import static com.moon.constant.RedisConstant.*;
import static com.moon.enums.ArticleStatusEnum.PUBLIC;

/**
 * 博客信息接口实现类
 *
 * @author:Y.0
 * @date:2023/9/23
 */
@Service
@Slf4j
public class BlogReportServiceImpl  implements BlogReportService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private HttpServletRequest request;

    @Autowired(required = false)
    private ArticleMapper articleMapper;

    @Autowired(required = false)
    private CategoryMapper categoryMapper;

    @Autowired(required = false)
    private TagMapper tagMapper;

    @Autowired(required = false)
    private SiteConfigService siteConfigService;

    @Autowired(required = false)
    private MessageMapper messageMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private VisitLogMapper visitLogMapper;
    /**
     * 上传访客信息
     *
     */
    @Override
    public void report() {
        // 获取用户ip
        String ipAddress = IpUtils.getIpAddress(request);
        Map<String, String> userAgentMap = UserAgentUtils.parseOsAndBrowser(request.getHeader("User-Agent"));
        // 获取访问设备
        String browser = userAgentMap.get("browser");
        String os = userAgentMap.get("os");
        // 生成唯一用户标识
        String uuid = ipAddress + browser + os;
        String md5 = DigestUtils.md5DigestAsHex(uuid.getBytes());
        // 判断是否访问
        if (!redisService.hasSetValue(UNIQUE_VISITOR, md5)) {
            // 访问量+1
            redisService.incr(BLOG_VIEW_COUNT, 1);
            // 保存唯一标识
            redisService.setSet(UNIQUE_VISITOR, md5);
        }
    }

    /**
     * 查看博客信息
     *
     * @return
     */
    @Override
    public BlogVo getBlog() {
        //查询文章数量
        Long articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, PUBLIC.getStatus())
                .eq(Article::getIsDelete, FALSE));
        //查询分类数量
        Long categoryCount = categoryMapper.selectCount(null);
        //查询标签数量
        Long tagCount = tagMapper.selectCount(null);
        //博客访问量
        Integer count = redisService.getObject(BLOG_VIEW_COUNT);
        String viewCount = Optional.ofNullable(count).orElse(0).toString();
        //网络配置
        SiteConfig siteConfig = siteConfigService.getSiteConfig();
        return BlogVo.builder()
                .articleCount(articleCount)
                .categoryCount(categoryCount)
                .tagCount(tagCount)
                .viewCount(viewCount)
                .siteConfig(siteConfig)
                .build();
    }

    /**
     * 查看我的信息
     *
     * @return
     */
    @Override
    public String getAbout() {
        SiteConfig siteConfig = redisService.getObject(SITE_SETTING);
        return siteConfig.getAboutMe();
    }

    /**
     * 查看后台信息
     *
     * @return
     */
    @Override
    public BlogReportVo getBlogReport() {
        // 访问量
        Integer viewCount = redisService.getObject(BLOG_VIEW_COUNT);
        // 留言量
        Long messageCount = messageMapper.selectCount(null);
        // 用户量
        Long userCount = userMapper.selectCount(null);
        // 文章量
        Long articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, FALSE));
        // 分类数据
        List<CategoryVo> categoryVOList = categoryMapper.selectCategoryVO();
        // 标签数据
        List<TagOptionVo> tagVOList = tagMapper.selectTagOptionList();
        // 查询用户浏览
        DateTime startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -7));
        DateTime endTime = DateUtil.endOfDay(new Date());
        List<UserViewVo> userViewVOList = visitLogMapper.selectUserViewList(startTime, endTime);
        // 文章统计
        List<ArticleStatisticsVo> articleStatisticsList = articleMapper.selectArticleStatistics();
        // 查询redis访问量前五的文章
        Map<Object, Double> articleMap = redisService.zReverseRangeWithScore(ARTICLE_VIEW_COUNT, 0, 4);
        BlogReportVo blogBackInfoVO = BlogReportVo.builder()
                .articleStatisticsList(articleStatisticsList)
                .tagVOList(tagVOList)
                .viewCount(viewCount)
                .messageCount(messageCount)
                .userCount(userCount)
                .articleCount(articleCount)
                .categoryVOList(categoryVOList)
                .userViewVOList(userViewVOList)
                .build();
        if (CollectionUtils.isNotEmpty(articleMap)) {
            // 查询文章排行
            List<ArticleRankVo> articleRankVOList = listArticleRank(articleMap);
            blogBackInfoVO.setArticleRankVOList(articleRankVOList);
        }
        return blogBackInfoVO;
    }

    /**
     * 查询文章排行
     *
     * @param articleMap 文章浏览量信息
     * @return {@link List<ArticleRankVo>} 文章排行
     */
    private List<ArticleRankVo> listArticleRank(Map<Object, Double> articleMap) {
        // 提取文章id
        List<Integer> articleIdList = new ArrayList<>(articleMap.size());
        articleMap.forEach((key, value) -> articleIdList.add((Integer) key));
        // 查询文章信息
        List<Article> articleList = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle)
                .in(Article::getId, articleIdList));
        return articleList.stream()
                .map(article -> ArticleRankVo.builder()
                        .articleTitle(article.getArticleTitle())
                        .viewCount(articleMap.get(article.getId()).intValue())
                        .build())
                .sorted(Comparator.comparingInt(ArticleRankVo::getViewCount).reversed())
                .collect(Collectors.toList());
    }
}
