package com.moon.strategy.context;

import com.moon.strategy.UploadStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.util.Map;

import static com.moon.enums.UploadModeEnum.getStrategy;

/**
 * 上传策略上下文
 *
 * @author:Y.0
 * @date:2023/10/9
 */

@Service
public class UploadStrategyContext {

    /**
     * 上传模式
     */
    @Value("${upload.strategy}")  //oss
    private String uploadStrategy;

    @Autowired(required = false)
    private Map<String, UploadStrategyService> uploadStrategyServiceMap;

    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 路径
     * @return {@link String} 文件地址
     */
    public String executeUploadStrategy(MultipartFile file, String path) {
        return uploadStrategyServiceMap.get(getStrategy(uploadStrategy)).uploadFile(file, path);
    }
}
