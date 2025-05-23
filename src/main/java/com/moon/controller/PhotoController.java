package com.moon.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.moon.annotation.OptLogger;
import com.moon.annotation.VisitLogger;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.PhotoDto;
import com.moon.entity.dto.PhotoInfoDto;
import com.moon.entity.vo.AlbumBackVo;
import com.moon.entity.vo.PageResult;
import com.moon.entity.vo.PhotoBackVo;
import com.moon.service.PhotoService;
import com.moon.service.RedisService;
import com.moon.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static com.moon.constant.OptTypeConstant.*;

/**
 * 照片模块管理
 *
 * @author:Y.0
 * @date:2023/10/14
 */

@Api(tags = "照片模块管理")
@RestController()
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    /**
     * 查看照片列表
     *
     * @param conditionDto
     * @return
     */
    @VisitLogger(value = "照片")
    @ApiOperation("查看照片列表")
    @GetMapping("/photo/list")
    public Result<Map<String,Object>> photoList(ConditionDto conditionDto){
        return Result.success(photoService.photoList(conditionDto));
    }

    /**
     * 查看后台照片列表
     *
     * @param condition 条件
     * @return 后台照片列表
     */
    @ApiOperation(value = "查看后台照片列表")
    @SaCheckPermission("web:photo:list")
    @GetMapping("/admin/photo/list")
    public Result<PageResult<PhotoBackVo>> listPhotoBackVO(ConditionDto condition) {
        return Result.success(photoService.photoListVo(condition));
    }

    /**
     * 查看照片相册信息
     *
     * @param albumId 相册id
     * @return 相册信息
     */
    @ApiOperation(value = "查看照片相册信息")
    @SaCheckPermission("web:photo:list")
    @GetMapping("/admin/photo/album/{albumId}/info")
    public Result<AlbumBackVo> getAlbumInfo(@PathVariable("albumId") Integer albumId) {
        return Result.success(photoService.getAlbumInfo(albumId));
    }

    /**
     * 上传照片
     *
     * @param file 文件
     * @return {@link Result<String>} 照片地址
     */
    @OptLogger(value = UPLOAD)
    @ApiOperation(value = "上传照片")
    @ApiImplicitParam(name = "file", value = "照片", required = true, dataType = "MultipartFile")
    @SaCheckPermission("web:photo:upload")
    @PostMapping("/admin/photo/upload")
    public Result<String> uploadPhoto(@RequestParam("file") MultipartFile file) {
        return Result.success(photoService.uploadPhoto(file));
    }

    /**
     * 添加照片
     *
     * @param photo 照片
     * @return {@link Result<>}
     */
    @OptLogger(value = ADD)
    @ApiOperation(value = "添加照片")
    @SaCheckPermission("web:photo:add")
    @PostMapping("/admin/photo/add")
    public Result<?> addPhoto(@Validated @RequestBody PhotoDto photo) {
        photoService.addPhoto(photo);
        return Result.success();
    }

    /**
     * 修改照片信息
     *
     * @param photoInfo 照片信息
     * @return {@link Result<>}
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "修改照片信息")
    @SaCheckPermission("web:photo:update")
    @PutMapping("/admin/photo/update")
    public Result<?> updatePhoto(@Validated @RequestBody PhotoInfoDto photoInfo) {
        photoService.updatePhoto(photoInfo);
        return Result.success();
    }

    /**
     * 删除照片
     *
     * @param photoIdList 照片id列表
     * @return {@link Result<>}
     */
    @OptLogger(value = DELETE)
    @ApiOperation(value = "删除照片")
    @SaCheckPermission("web:photo:delete")
    @DeleteMapping("/admin/photo/delete")
    public Result<?> deletePhoto(@RequestBody List<Integer> photoIdList) {
        photoService.deletePhoto(photoIdList);
        return Result.success();
    }

    /**
     * 移动照片
     *
     * @param photo 照片信息
     * @return {@link Result<>}
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "移动照片")
    @SaCheckPermission("web:photo:move")
    @PutMapping("/admin/photo/move")
    public Result<?> movePhoto(@Validated @RequestBody PhotoDto photo) {
        photoService.movePhoto(photo);
        return Result.success();
    }

}
