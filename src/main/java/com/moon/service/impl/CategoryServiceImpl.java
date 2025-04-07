package com.moon.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.entity.Article;
import com.moon.entity.Category;
import com.moon.entity.dto.CategoryDto;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.vo.*;
import com.moon.mapper.ArticleMapper;
import com.moon.service.CategoryService;
import com.moon.mapper.CategoryMapper;
import com.moon.utils.BeanCopyUtils;
import com.moon.utils.PageUtils;
import com.moon.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
* @author Y.0
* @description 针对表【t_category】的数据库操作Service实现
* @createDate 2023-10-13 17:12:50
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

    @Autowired(required = false)
    private CategoryMapper categoryMapper;

    @Autowired(required = false)
    private ArticleMapper articleMapper;

    /**
     * 查看分类列表
     *
     * @return
     */
    @Override
    public List<CategoryVo> CategoryVoList() {
        return categoryMapper.selectCategoryVO();
    }

    /**
     * 查看分类下的文章
     *
     * @param condition 查询条件
     * @return 文章列表
     */
    @Override
    public ArticleCondListVo ArticleCategoryList(ConditionDto condition) {
        List<ArticleCondition> articleConditionList = articleMapper.listArticleByCondition(PageUtils.getLimit(), PageUtils.getSize(), condition);
        String name = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                        .select(Category::getCategoryName)
                        .eq(Category::getId, condition.getCategoryId()))
                .getCategoryName();
        return ArticleCondListVo.builder()
                .articleConditionVOList(articleConditionList)
                .name(name)
                .build();
    }

    /**
     * 查看分类选项
     *
     * @return
     */
    @Override
    public List<CategoryOptionVo> categoryOptionList() {
        // 查询分类
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .orderByDesc(Category::getId));
        return BeanCopyUtils.copyBeanList(categoryList, CategoryOptionVo.class);
    }

    /**
     * 修改分类
     *
     * @param categoryDto 分类信息
     * @return {@link Result <>}
     */
    @Override
    public void updateCategory(CategoryDto categoryDto) {
        // 分类是否存在
        Category existCategory = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getId)
                .eq(Category::getCategoryName, categoryDto.getCategoryName()));
        Assert.isFalse(Objects.nonNull(existCategory) && !existCategory.getId().equals(categoryDto.getId()),
                categoryDto.getCategoryName() + "分类已存在");
        // 修改分类
        Category newCategory = Category.builder()
                .id(categoryDto.getId())
                .categoryName(categoryDto.getCategoryName())
                .build();
        baseMapper.updateById(newCategory);
    }

    /**
     * 添加分类
     *
     * @param categoryDto 分类信息
     * @return
     */
    @Override
    public void addCategory(CategoryDto categoryDto) {
        // 分类是否存在
        Category existCategory = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getId)
                .eq(Category::getCategoryName, categoryDto.getCategoryName()));
        Assert.isNull(existCategory, categoryDto.getCategoryName() + "分类已存在");
        // 添加新分类
        Category newCategory = Category.builder()
                .categoryName(categoryDto.getCategoryName())
                .build();
        baseMapper.insert(newCategory);
    }

    /**
     * 删除分类
     *
     * @param categoryIdList 分类id集合
     * @return
     */
    @Override
    public void deleteCategory(List<Integer> categoryIdList) {
        // 分类下是否有文章
        Long count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .in(Article::getCategoryId, categoryIdList));
        Assert.isFalse(count > 0, "删除失败，分类下存在文章");
        // 批量删除分类
        categoryMapper.deleteBatchIds(categoryIdList);
    }

    /**
     * 查看后台分类列表
     *
     * @param conditionDto 查询条件
     * @return 后台分类列表
     */
    @Override
    public PageResult<CategoryAdminVo> categoryVoList(ConditionDto conditionDto) {
        // 查询分类数量
        Long count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                .like(StringUtils.hasText(conditionDto.getKeyword()), Category::getCategoryName,
                        conditionDto.getKeyword()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询分类列表
        List<CategoryAdminVo> categoryList = categoryMapper.selectCategoryBackVO(PageUtils.getLimit(),
                PageUtils.getSize(), conditionDto.getKeyword());
        return new PageResult<>(categoryList, count);
    }
}




