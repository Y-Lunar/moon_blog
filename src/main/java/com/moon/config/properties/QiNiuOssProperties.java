package com.moon.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author:Y.0
 * @date:2023/12/10
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "upload.oss")
public class QiNiuOssProperties {


    /**
     * url 或者 域名
     */
    private String url;

    /**
     * 存储桶名字
     */
    private String bucketName;

    /**
     * 七牛云区域
     */
    private String region;

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

}
