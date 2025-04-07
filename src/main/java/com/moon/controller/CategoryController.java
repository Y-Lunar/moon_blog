package com.moon.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.moon.annotation.OptLogger;
import com.moon.annotation.VisitLogger;
import com.moon.entity.dto.CategoryDto;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.vo.*;
import com.moon.service.CategoryService;
import com.moon.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.moon.constant.OptTypeConstant.*;

/**
 * 分类模块管理
 *
 * @author:Y.0
 * @date:2023/10/13
 */

@Api(tags = "分类模块管理")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查看分类列表
     *
     * @return
     */
    @VisitLogger(value = "文章分类")
    @ApiOperation(value = "查看分类列表")
    @GetMapping("/category/list")
    public Result<List<CategoryVo>> CategoryVoList() {
        return Result.success(categoryService.CategoryVoList());
    }

    /**
     * 查看分类下的文章
     *
     * @param condition 查询条件
     * @return 文章列表
     */
    @VisitLogger(value = "分类文章")
    @ApiOperation(value = "查看分类下的文章")
    @GetMapping("/category/article")
    public Result<ArticleCondListVo> ArticleCategoryList(ConditionDto condition) {
        return Result.success(categoryService.ArticleCategoryList(condition));
    }

    /**
     * 查看分类选项
     *
     * @return
     */
    @ApiOperation(value = "查看分类选项")
    @GetMapping("/admin/category/option")
    public Result<List<CategoryOptionVo>> CategoryOptionList() {
        return Result.success(categoryService.categoryOptionList());
    }

    /**
     * 修改分类
     *
     * @param categoryDto 分类信息
     * @return {@link Result<>}
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "修改分类")
    @SaCheckPermission("blog:category:update")
    @PutMapping("/admin/category/update")
    public Result<?> updateCategory(@Validated @RequestBody CategoryDto categoryDto) {
        categoryService.updateCategory(categoryDto);
        return Result.success();
    }

    /**
     * 添加分类
     *
     * @param categoryDto 分类信息
     * @return
     */
    @OptLogger(value = ADD)
    @ApiOperation(value = "添加分类")
    @SaCheckPermission("blog:category:add")
    @PostMapping("/admin/category/add")
    public Result<?> addCategory(@Validated @RequestBody CategoryDto categoryDto) {
        categoryService.addCategory(categoryDto);
        return Result.success();
    }

    /**
     * 删除分类
     *
     * @param categoryIdList 分类id集合
     * @return
     */
    @OptLogger(value = DELETE)
    @ApiOperation(value = "删除分类")
    @SaCheckPermission("blog:category:delete")
    @DeleteMapping("/admin/category/delete")
    public Result<?> deleteCategory(@RequestBody List<Integer> categoryIdList) {
        categoryService.deleteCategory(categoryIdList);
        return Result.success();
    }

    /**
     * 查看后台分类列表
     *
     * @param conditionDto 查询条件
     * @return
     */
    @ApiOperation(value = "查看后台分类列表")
    @SaCheckPermission("blog:category:list")
    @GetMapping("/admin/category/list")
    public Result<PageResult<CategoryAdminVo>> categoryVoList(ConditionDto conditionDto) {
        PageResult<CategoryAdminVo> categoryAdminVoPageResult = categoryService.categoryVoList(conditionDto);
        return Result.success(categoryAdminVoPageResult);
    }
}
