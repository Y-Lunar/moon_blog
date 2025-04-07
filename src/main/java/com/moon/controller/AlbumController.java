package com.moon.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moon.annotation.OptLogger;
import com.moon.annotation.VisitLogger;
import com.moon.entity.dto.AlbumDto;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.vo.AlbumBackVo;
import com.moon.entity.vo.AlbumVo;
import com.moon.entity.vo.PageResult;
import com.moon.service.AlbumService;
import com.moon.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.moon.constant.OptTypeConstant.*;

/**
 * 相册模块管理
 *
 * @author:Y.0
 * @date:2023/10/13
 */

@Api(tags = "相册模块管理")
@RestController
public class AlbumController {

    @Autowired
    private AlbumService albumService;


    /**
     * 查看相册列表
     *
     * @return
     */
    @VisitLogger(value = "相册")
    @ApiOperation(value = "查看相册列表")
    @GetMapping("/album/list")
    public Result<List<AlbumVo>> AlbumVoList() {
        return Result.success(albumService.AlbumVoList());
    }


    /**
     * 查看后台相册列表
     *
     * @param condition 条件
     * @return
     */
    @ApiOperation(value = "查看后台相册列表")
    @SaCheckPermission("web:album:list")
    @GetMapping("/admin/album/list")
    public Result<PageResult<AlbumBackVo>> listAlbumBackVO(ConditionDto condition) {
        PageResult<AlbumBackVo> backVoPageResult = albumService.albumBackVoList(condition);
        return Result.success(backVoPageResult);
    }

    /**
     * 上传相册封面
     *
     * @param file 文件
     * @return 相册封面地址
     */
    @OptLogger(value = UPLOAD)
    @ApiOperation(value = "上传相册封面")
    @ApiImplicitParam(name = "file", value = "相册封面", required = true, dataType = "MultipartFile")
    @SaCheckPermission("web:album:upload")
    @PostMapping("/admin/album/upload")
    public Result<String> uploadAlbumCover(@RequestParam("file") MultipartFile file) {
        return Result.success(albumService.uploadAlbumCover(file));
    }

    /**
     * 添加相册
     *
     * @param album 相册信息
     * @return
     */
    @OptLogger(value = ADD)
    @ApiOperation(value = "添加相册")
    @SaCheckPermission("web:album:add")
    @PostMapping("/admin/album/add")
    public Result<?> addAlbum(@Validated @RequestBody AlbumDto album) {
        albumService.addAlbum(album);
        return Result.success();
    }

    /**
     * 删除相册
     *
     * @param albumId 相册id
     * @return
     */
    @OptLogger(value = DELETE)
    @ApiOperation(value = "删除相册")
    @SaCheckPermission("web:album:delete")
    @DeleteMapping("/admin/album/delete/{albumId}")
    public Result<?> deleteAlbum(@PathVariable("albumId") Integer albumId) {
        albumService.deleteAlbum(albumId);
        return Result.success();
    }

    /**
     * 修改相册
     *
     * @param album 相册信息
     * @return
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "修改相册")
    @SaCheckPermission("web:album:update")
    @PutMapping("/admin/album/update")
    public Result<?> updateAlbum(@Validated @RequestBody AlbumDto album) {
        albumService.updateAlbum(album);
        return Result.success();
    }

    /**
     * 编辑相册
     *
     * @param albumId 相册id
     * @return 相册
     */
    @ApiOperation(value = "编辑相册")
    @SaCheckPermission("web:album:edit")
    @GetMapping("/admin/album/edit/{albumId}")
    public Result<AlbumDto> editAlbum(@PathVariable("albumId") Integer albumId) {
        return Result.success(albumService.editAlbum(albumId));
    }
}
