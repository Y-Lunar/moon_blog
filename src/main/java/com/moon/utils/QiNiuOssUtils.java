package com.moon.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static com.moon.enums.UploadFileEnum.UPLOAD_FILE_LARGE;


/**
 * 七牛云OSS工具
 *
 * @author:Y.0
 * @date:2023/9/21
 */
@Component
public class QiNiuOssUtils {

    @Value("${upload.oss.accessKey}")
    private String accessKey;

    @Value("${upload.oss.secretKey}")
    private String secretKey;

    @Value("${upload.oss.bucketName}")
    private String bucket;

    @Value("${upload.oss.url}")
    private String url;


    public String uploadOss(MultipartFile imgFile, String filePath) throws RuntimeException {
        // 获取文件大小
        long fileSize = imgFile.getSize();

        // 判断文件大小是否超过2MB（2MB=2*1024*1024 bytes）
        // 抛出文件大小超过限制的异常
        if (fileSize > 2 * 1024 * 1024) {
            throw new RuntimeException(String.valueOf(UPLOAD_FILE_LARGE));
        }
        //构造一个带指定 Region 对象的配置类。你的七牛云OSS创建的是哪个区域的，那么就调用Region的什么方法即可
        //可以使用autoRegion自动配置区域
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        UploadManager uploadManager = new UploadManager(cfg);

        //文件名，如果写成null的话，就以文件内容的hash值作为文件名
        String key = filePath;
        try{
            //把前端传过来的文件转换成InputStream对象
            InputStream FileinputStream = imgFile.getInputStream();

            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(FileinputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet =
                        new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println("上传成功! 生成的key是: "+putRet.key);
                System.out.println("上传成功! 生成的hash是: "+putRet.hash);
                //http://s0cyicjxs.hd-bkt.clouddn.com/2023-09-02-05b7db37d41e4f6cae2e75862b66bbba.png
                return url + filePath;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
            return "上传失败";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //上传文件
    public void uploadOss(byte[] bytes, String fileName){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(bytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet =
                    new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    //删除文件
    public void deleteFileFromQiniu(String fileName){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }
}
