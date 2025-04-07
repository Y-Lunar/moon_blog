package com.moon.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.moon.annotation.OptLogger;
import com.moon.annotation.VisitLogger;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.TagDto;
import com.moon.entity.vo.*;
import com.moon.service.TagService;
import com.moon.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.moon.constant.OptTypeConstant.*;

/**
 * 标签管理模块
 *
 * @author:Y.0
 * @date:2023/10/14
 */

@Api(tags = "标签管理模块")
@RestController
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 文章标签列表
     *
     * @return
     */
    @VisitLogger(value = "文章标签")
    @ApiOperation("文章标签列表")
    @GetMapping("/tag/list")
    public Result<List<TagVo>> tagList(){
        List<TagVo> tagVoList = tagService.selectTagList();
        return Result.success(tagVoList);
    }

    /**
     * 查看标签下的文章信息
     *
     * @param conditionDto 查询条件
     * @return
     */
    @VisitLogger(value = "标签文章")
    @ApiOperation("查看标签下的文章信息")
    @GetMapping("/tag/article")
    public Result<ArticleCondListVo> ArticleTagList(ConditionDto conditionDto){
        ArticleCondListVo articleCondListVo = tagService.articleTagList(conditionDto);
        return Result.success(articleCondListVo);
    }

    /**
     * 查看后台标签列表
     *
     * @param condition 查询条件
     * @return
     */
    @ApiOperation(value = "查看后台标签列表")
    @SaCheckPermission("blog:tag:list")
    @GetMapping("/admin/tag/list")
    public Result<PageResult<TagBackVo>> listTagBackVO(ConditionDto condition) {
        PageResult<TagBackVo> tagBackVoPageResult = tagService.tagBackVoList(condition);
        return Result.success(tagBackVoPageResult);
    }

    /**
     * 添加标签
     *
     * @param tagDto 标签信息
     * @return
     */
    @OptLogger(value = ADD)
    @ApiOperation(value = "添加标签")
    @SaCheckPermission("blog:tag:add")
    @PostMapping("/admin/tag/add")
    public Result<?> addTag(@Validated @RequestBody TagDto tagDto) {
        tagService.addTag(tagDto);
        return Result.success();
    }

    /**
     * 删除标签
     *
     * @param tagIdList 标签id集合
     * @return
     */
    @OptLogger(value = DELETE)
    @ApiOperation(value = "删除分类")
    @SaCheckPermission("blog:tag:delete")
    @DeleteMapping("/admin/tag/delete")
    public Result<?> deleteTag(@RequestBody List<Integer> tagIdList) {
        tagService.deleteTag(tagIdList);
        return Result.success();
    }

    /**
     * 修改标签
     *
     * @param tagDto 标签信息
     * @return
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "修改标签")
    @SaCheckPermission("blog:tag:update")
    @PutMapping("/admin/tag/update")
    public Result<?> updateTag(@Validated @RequestBody TagDto tagDto) {
        tagService.updateTag(tagDto);
        return Result.success();
    }

    /**
     * 查看标签选项
     *
     * @return
     */
    @ApiOperation(value = "查看标签选项")
    @GetMapping("/admin/tag/option")
    public Result<List<TagOptionVo>> listTagOption() {
        return Result.success(tagService.listTagOption());
    }
}
