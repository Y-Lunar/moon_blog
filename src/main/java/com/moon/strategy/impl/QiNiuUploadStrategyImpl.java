package com.moon.strategy.impl;

import com.google.gson.Gson;
import com.moon.config.properties.QiNiuOssProperties;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @author:Y.0
 * @date:2023/12/10
 */

@Slf4j
@Service("qiNiuUploadStrategyImpl")
public class QiNiuUploadStrategyImpl extends AbstractUploadStrategyImpl{

    @Autowired
    private QiNiuOssProperties qiNiuOssProperties;

    @Override
    public Boolean exists(String filePath) {
        return false;
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream) {
        try {
            if (path.length() > 1 && path.charAt(0) == '/') {
                path = path.substring(1);
            }
            // 认证信息实例
            Auth auth = Auth.create(qiNiuOssProperties.getAccessKey(), qiNiuOssProperties.getSecretKey());
            //构造一个带指定 Region 对象的配置类。你的七牛云OSS创建的是哪个区域的，那么就调用Region的什么方法即可
            //可以使用autoRegion自动配置区域
            Configuration cfg = new Configuration(Region.autoRegion());
            cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
            UploadManager uploadManager = new UploadManager(cfg);

            //文件名，如果写成null的话，就以文件内容的hash值作为文件名
            String key = path + fileName;
            String upToken = auth.uploadToken(qiNiuOssProperties.getBucketName());
            // 上传图片文件
            Response res = uploadManager.put(inputStream, key, upToken, null, null);
            //解析上传成功的结果
            DefaultPutRet putRet =
                    new Gson().fromJson(res.bodyString(), DefaultPutRet.class);
            System.out.println("上传成功! 生成的key是: "+putRet.key);
            System.out.println("上传成功! 生成的hash是: "+putRet.hash);
            if (!res.isOK()) {
                throw new RuntimeException("上传七牛出错：" + res);
            }
        } catch (Exception e) {
            log.error("上传七牛出错，{}", e.getMessage());
        }
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        // 1./  --> /    2./xxx/ --> xxx/
        if (filePath.length() > 1 && filePath.charAt(0) == '/') {
            filePath = filePath.substring(1);
        }
        return qiNiuOssProperties.getUrl() + filePath;
    }

    /**
     * 配置空间的存储区域
     */
    private com.qiniu.storage.Configuration qiNiuConfig() {
        switch (qiNiuOssProperties.getRegion()) {
            case "huadong":
                return new com.qiniu.storage.Configuration(Region.huadong());
            case "huabei":
                return new com.qiniu.storage.Configuration(Region.huabei());
            case "huanan":
                return new com.qiniu.storage.Configuration(Region.huanan());
            case "beimei":
                return new com.qiniu.storage.Configuration(Region.beimei());
            default:
                throw new RuntimeException("存储区域配置错误");
        }
    }

}
