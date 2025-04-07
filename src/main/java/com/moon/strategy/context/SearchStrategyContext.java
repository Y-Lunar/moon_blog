package com.moon.strategy.context;

import com.moon.entity.vo.ArticleSearchVo;
import com.moon.strategy.SearchStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.moon.enums.SearchModeEnum.getStrategy;

/**
 * 搜索策略上下文
 *
 * @author:Y.0
 * @date:2023/10/24
 */

@Service
public class SearchStrategyContext {

    /**
     * 搜索模式
     */
//    @Value("${search.mode}")
//    private String searchMode;

    @Autowired(required = false)
    private Map<String, SearchStrategy> searchStrategyMap;

    /**
     * 执行搜索策略
     *
     * @param keyword 关键字
     * @return {@link List <ArticleSearchVO>} 搜索文章
     */
    public List<ArticleSearchVo> executeSearchStrategy(String keyword) {
        return searchStrategyMap.get(getStrategy("mysql")).searchArticle(keyword);
    }

}
