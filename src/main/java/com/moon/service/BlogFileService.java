package com.moon.service;

import com.moon.entity.BlogFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.FolderDto;
import com.moon.entity.vo.FileVO;
import com.moon.entity.vo.PageResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_blog_file】的数据库操作Service
* @createDate 2023-10-15 17:28:24
*/
public interface BlogFileService extends IService<BlogFile> {
    /**
     * 查看文件列表
     *
     * @param condition 查询条件
     * @return 文件列表
     */
    PageResult<FileVO> listFileVOList(ConditionDto condition);

    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 文件路径
     */
    void uploadFile(MultipartFile file, String path);

    /**
     * 创建文件夹
     *
     * @param folder 文件夹信息
     */
    void createFolder(FolderDto folder);

    /**
     * 删除文件
     *
     * @param fileIdList 文件id列表
     */
    void deleteFile(List<Integer> fileIdList);

    /**
     * 下载文件
     *
     * @param fileId 文件id
     */
    void downloadFile(Integer fileId);
}
