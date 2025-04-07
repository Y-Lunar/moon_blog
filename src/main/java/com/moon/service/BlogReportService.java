package com.moon.service;

import com.moon.entity.vo.BlogReportVo;
import com.moon.entity.vo.BlogVo;

/**
 * 博客信息接口
 *
 * @author:Y.0
 * @date:2023/9/23
 */
public interface BlogReportService {

    /**
     * 上传访客信息
     */
    void report();

    /**
     * 查看博客信息
     * @return
     */
    BlogVo getBlog();

    /**
     * 查看我的信息
     *
     * @return
     */
    String getAbout();

    /**
     * 查看后台信息
     *
     * @return
     */
    BlogReportVo getBlogReport();
}
