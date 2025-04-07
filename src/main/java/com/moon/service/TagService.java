package com.moon.service;

import com.moon.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.TagDto;
import com.moon.entity.vo.*;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_tag】的数据库操作Service
* @createDate 2023-09-23 09:56:47
*/
public interface TagService extends IService<Tag> {

    /**
     * 文章标签列表
     *
     * @return
     */
    List<TagVo> selectTagList();

    /**
     * 查看标签下的文章信息
     *
     * @param conditionDto 查询条件
     * @return
     */
    ArticleCondListVo articleTagList(ConditionDto conditionDto);

    /**
     * 查看后台标签列表
     *
     * @param condition 查询条件
     * @return
     */
    PageResult<TagBackVo> tagBackVoList(ConditionDto condition);

    /**
     * 添加标签
     *
     * @param tagDto 标签信息
     * @return
     */
    void addTag(TagDto tagDto);

    /**
     * 删除标签
     *
     * @param tagIdList 标签id集合
     * @return
     */
    void deleteTag(List<Integer> tagIdList);

    /**
     * 修改标签
     *
     * @param tagDto 标签信息
     * @return
     */
    void updateTag(TagDto tagDto);

    /**
     * 查看标签选项
     *
     * @return
     */
    List<TagOptionVo> listTagOption();
}
