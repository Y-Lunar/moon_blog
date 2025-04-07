package com.moon.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.moon.annotation.OptLogger;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.FolderDto;
import com.moon.entity.vo.FileVO;
import com.moon.entity.vo.PageResult;
import com.moon.service.BlogFileService;
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
 * 文件控制器
 *
 * @author:Y.0
 * @date:2023/10/24
 **/
@Api(tags = "文件模块")
@RestController
public class BlogFileController {

    @Autowired
    private BlogFileService fileService;

    /**
     * 查看文件列表
     *
     * @param condition 查询条件
     * @return 文件列表
     */
    @ApiOperation(value = "查看文件列表")
    @SaCheckPermission("system:file:list")
    @GetMapping("/admin/file/list")
    public Result<PageResult<FileVO>> listFileVOList(ConditionDto condition) {
        PageResult<FileVO> fileVoPageResult = fileService.listFileVOList(condition);
        return Result.success(fileVoPageResult);
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @return
     */
    @OptLogger(value = UPLOAD)
    @ApiOperation(value = "上传文件")
    @ApiImplicitParam(name = "file", value = "图片", required = true, dataType = "MultipartFile")
    @SaCheckPermission("system:file:upload")
    @PostMapping("/admin/file/upload")
    public Result<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) {
        fileService.uploadFile(file, path);
        return Result.success();
    }

    /**
     * 创建目录
     *
     * @param folder 目录信息
     * @return
     */
    @OptLogger(value = ADD)
    @ApiOperation(value = "创建目录")
    @SaCheckPermission("system:file:createFolder")
    @PostMapping("/admin/file/createFolder")
    public Result<?> createFolder(@Validated @RequestBody FolderDto folder) {
        fileService.createFolder(folder);
        return Result.success();
    }

    /**
     * 删除文件
     *
     * @param fileIdList 文件id列表
     * @return
     */
    @OptLogger(value = DELETE)
    @ApiOperation(value = "删除文件")
    @SaCheckPermission("system:file:delete")
    @DeleteMapping("/admin/file/delete")
    public Result<?> deleteFile(@RequestBody List<Integer> fileIdList) {
        fileService.deleteFile(fileIdList);
        return Result.success();
    }

    /**
     * 下载文件
     *
     * @param fileId 文件id
     * @return
     */
    @ApiOperation(value = "下载文件")
    @GetMapping("/file/download/{fileId}")
    public Result<?> downloadFile(@PathVariable("fileId") Integer fileId) {
        fileService.downloadFile(fileId);
        return Result.success();
    }

}