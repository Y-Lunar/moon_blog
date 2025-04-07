package com.moon.service;

import com.moon.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.entity.dto.CategoryDto;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.vo.*;
import com.moon.utils.Result;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_category】的数据库操作Service
* @createDate 2023-10-13 17:12:50
*/
public interface CategoryService extends IService<Category> {

    /**
     * 查看分类列表
     *
     * @return
     */
    List<CategoryVo> CategoryVoList();

    /**
     * 查看分类下的文章
     *
     * @param condition 查询条件
     * @return 文章列表
     */
    ArticleCondListVo ArticleCategoryList(ConditionDto condition);

    /**
     * 查看分类选项
     *
     * @return
     */
    List<CategoryOptionVo> categoryOptionList();

    /**
     * 修改分类
     *
     * @param categoryDto 分类信息
     * @return {@link Result <>}
     */
    void updateCategory(CategoryDto categoryDto);

    /**
     * 添加分类
     *
     * @param categoryDto 分类信息
     * @return
     */
    void addCategory(CategoryDto categoryDto);

    /**
     * 删除分类
     *
     * @param categoryIdList 分类id集合
     * @return
     */
    void deleteCategory(List<Integer> categoryIdList);


    /**
     * 查看后台分类列表
     *
     * @param conditionDto 查询条件
     * @return
     */
    PageResult<CategoryAdminVo> categoryVoList(ConditionDto conditionDto);
}
