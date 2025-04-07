package com.moon.mapper;

import com.moon.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moon.entity.vo.TagBackVo;
import com.moon.entity.vo.TagOptionVo;
import com.moon.entity.vo.TagVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_tag】的数据库操作Mapper
* @createDate 2023-09-23 09:56:47
* @Entity com.moon.entity.Tag
*/
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 查询标签列表
     *
     * @return 标签列表
     */
    List<TagOptionVo> selectTagOptionList();

    /**
     * 文章标签列表
     *
     * @return
     */
    List<TagVo> selectTagList();

    /**
     * 根据标签名查询标签
     *
     * @param tagNameList 标签名列表
     * @return 标签
     */
    List<Tag> selectTagListRe(List<String> tagNameList);

    /**
     * 根据文章id查询文章标签名称
     *
     * @param articleId 文章id
     * @return 文章标签名称
     */
    List<String> selectTagNameByArticleId(@Param("articleId") Integer articleId);

    /**
     * 查询后台标签列表
     *
     * @param limit   页码
     * @param size    大小
     * @param keyword 关键字
     * @return 后台标签列表
     */
    List<TagBackVo> selectTagBackVO(@Param("limit") Long limit, @Param("size") Long size, @Param("keyword") String keyword);
}




