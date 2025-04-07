package com.moon.controller;

import com.moon.annotation.VisitLogger;
import com.moon.entity.vo.BlogReportVo;
import com.moon.entity.vo.BlogVo;
import com.moon.service.BlogReportService;
import com.moon.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 博客模块
 *
 * @author:Y.0
 * @date:2023/9/23
 */

@RestController
@Slf4j
@Api(tags = "博客模块")
public class BlogReportController {

    @Autowired
    private BlogReportService blogReportService;

    /**
     * 上传访客信息
     *
     * @return
     */
    @ApiOperation(value = "上传访客信息")
    @PostMapping("/report")
    public Result<?> report(){
        blogReportService.report();
        return Result.success();
    }

    /**
     * 查看博客信息
     *
     * @return 博客
     */
    @ApiOperation(value = "查看博客信息")
    @GetMapping("/")
    public Result<BlogVo> getBlogInfo(){
        return Result.success(blogReportService.getBlog());
    }

    /**
     * 查看关于我的信息
     *
     * @return
     */
    @VisitLogger(value = "关于")
    @ApiOperation(value = "查看关于我的信息")
    @GetMapping("/about")
    public Result<String> getAbout(){
        return Result.success(blogReportService.getAbout());
    }

    /**
     * 查看后台信息
     *
     * @return
     */
    @ApiOperation(value = "查看后台信息")
    @GetMapping("/admin")
    public Result<BlogReportVo> getBlogBackInfo() {
        return Result.success(blogReportService.getBlogReport());
    }
}
