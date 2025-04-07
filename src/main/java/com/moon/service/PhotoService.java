package com.moon.service;

import com.moon.entity.Photo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.PhotoDto;
import com.moon.entity.dto.PhotoInfoDto;
import com.moon.entity.vo.AlbumBackVo;
import com.moon.entity.vo.PageResult;
import com.moon.entity.vo.PhotoBackVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
* @author Y.0
* @description 针对表【t_photo】的数据库操作Service
* @createDate 2023-10-14 11:21:06
*/
public interface PhotoService extends IService<Photo> {

    /**
     * 查看照片列表
     *
     * @param conditionDto
     * @return
     */
    Map<String,Object> photoList(ConditionDto conditionDto);

    /**
     * 查看后台照片列表
     *
     * @param condition 条件
     * @return 后台照片列表
     */
    PageResult<PhotoBackVo> photoListVo(ConditionDto condition);

    /**
     * 查看照片相册信息
     *
     * @param albumId 相册id
     * @return 相册信息
     */
    AlbumBackVo getAlbumInfo(Integer albumId);

    /**
     * 添加照片
     *
     * @param photo 照片
     */
    void addPhoto(PhotoDto photo);

    /**
     * 修改照片信息
     *
     * @param photoInfo 照片信息
     */
    void updatePhoto(PhotoInfoDto photoInfo);

    /**
     * 删除照片
     *
     * @param photoIdList 照片id列表
     */
    void deletePhoto(List<Integer> photoIdList);

    /**
     * 移动照片
     *
     * @param photo 照片
     */
    void movePhoto(PhotoDto photo);

    /**
     * 上传照片
     *
     * @param file 文件
     * @return 照片地址
     */
    String uploadPhoto(MultipartFile file);
}
