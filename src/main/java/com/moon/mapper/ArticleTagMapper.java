package com.moon.mapper;

import com.moon.entity.ArticleTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moon.entity.vo.TagOptionVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_article_tag】的数据库操作Mapper
* @createDate 2023-10-15 17:14:46
* @Entity com.moon.entity.ArticleTag
*/
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

    /**
     * 批量保存文章标签
     *
     * @param articleId 文章id
     * @param tagIdList 标签id列表
     */
    void saveBatchArticleTag(@Param("articleId") Integer articleId, List<Integer> tagIdList);

    /**
     * 根据文章id查询出tagName标签名称
     * @param articleId
     * @return
     */
    List<TagOptionVo> selectTagName(@Param("articleId") Integer articleId);
}




