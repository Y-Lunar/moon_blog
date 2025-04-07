package com.moon.service;

import com.moon.entity.SiteConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author Y.0
* @description 针对表【t_site_config】的数据库操作Service
* @createDate 2023-09-23 09:57:14
*/
public interface SiteConfigService extends IService<SiteConfig> {

    /**
     * 获取网络配置
     *
     * @return 网络配置
     */
    SiteConfig getSiteConfig();

    /**
     * 更新网站配置
     *
     * @param siteConfig 网站配置
     * @return
     */
    void updateSiteConfig(SiteConfig siteConfig);

    /**
     * 上传网站配置图片
     *
     * @param file 图片
     * @return 图片路径
     */
    String uploadSiteImg(MultipartFile file);
}
