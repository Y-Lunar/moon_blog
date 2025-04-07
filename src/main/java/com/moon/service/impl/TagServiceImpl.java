package com.moon.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.entity.ArticleTag;
import com.moon.entity.Tag;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.TagDto;
import com.moon.entity.vo.*;
import com.moon.mapper.ArticleMapper;
import com.moon.mapper.ArticleTagMapper;
import com.moon.service.TagService;
import com.moon.mapper.TagMapper;
import com.moon.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
* @author Y.0
* @description 针对表【t_tag】的数据库操作Service实现
* @createDate 2023-09-23 09:56:47
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

    @Autowired(required = false)
    private TagMapper tagMapper;

    @Autowired(required = false)
    private ArticleMapper articleMapper;

    @Autowired(required = false)
    private ArticleTagMapper articleTagMapper;

    /**
     * 文章标签列表
     *
     * @return
     */
    @Override
    public List<TagVo> selectTagList() {
        return tagMapper.selectTagList();
    }

    /**
     * 查看标签下的文章信息
     *
     * @param conditionDto 查询条件
     * @return
     */
    @Override
    public ArticleCondListVo articleTagList(ConditionDto conditionDto) {
        List<ArticleCondition> articleConditionList = articleMapper.listArticleByCondition(PageUtils.getLimit(),
                PageUtils.getSize(), conditionDto);
        String name = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                        .select(Tag::getTagName)
                        .eq(Tag::getId, conditionDto.getTagId()))
                .getTagName();
        return ArticleCondListVo.builder()
                .articleConditionVOList(articleConditionList)
                .name(name)
                .build();
    }

    /**
     * 查看后台标签列表
     *
     * @param condition 查询条件
     * @return
     */
    @Override
    public PageResult<TagBackVo> tagBackVoList(ConditionDto condition) {
        // 查询标签数量
        Long count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                .like(StringUtils.hasText(condition.getKeyword()), Tag::getTagName,
                        condition.getKeyword()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询标签列表
        List<TagBackVo> tagList = tagMapper.selectTagBackVO(PageUtils.getLimit(), PageUtils.getSize(),
                condition.getKeyword());
        return new PageResult<>(tagList, count);
    }

    /**
     * 添加标签
     *
     * @param tagDto 标签信息
     * @return
     */
    @Override
    public void addTag(TagDto tagDto) {
        // 标签是否存在
        Tag existTag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId)
                .eq(Tag::getTagName, tagDto.getTagName()));
        Assert.isNull(existTag, tagDto.getTagName() + "标签已存在");
        // 添加新标签
        Tag newTag = Tag.builder()
                .tagName(tagDto.getTagName())
                .build();
        baseMapper.insert(newTag);
    }

    /**
     * 删除标签
     *
     * @param tagIdList 标签id集合
     * @return
     */
    @Override
    public void deleteTag(List<Integer> tagIdList) {
        // 标签下是否有文章
        Long count = articleTagMapper.selectCount(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getTagId, tagIdList));
        Assert.isFalse(count > 0, "删除失败，标签下存在文章");
        // 批量删除标签
        tagMapper.deleteBatchIds(tagIdList);
    }

    /**
     * 修改标签
     *
     * @param tagDto 标签信息
     * @return
     */
    @Override
    public void updateTag(TagDto tagDto) {
// 标签是否存在
        Tag existTag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId)
                .eq(Tag::getTagName, tagDto.getTagName()));
        Assert.isFalse(Objects.nonNull(existTag) && !existTag.getId().equals(tagDto.getId()),
                tagDto.getTagName() + "标签已存在");
        // 修改标签
        Tag newTag = Tag.builder()
                .id(tagDto.getId())
                .tagName(tagDto.getTagName())
                .build();
        baseMapper.updateById(newTag);
    }

    /**
     * 查看标签选项
     *
     * @return
     */
    @Override
    public List<TagOptionVo> listTagOption() {
        return tagMapper.selectTagOptionList();
    }
}




