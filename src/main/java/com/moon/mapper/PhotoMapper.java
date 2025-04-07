package com.moon.mapper;

import com.moon.entity.Photo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moon.entity.vo.PhotoBackVo;
import com.moon.entity.vo.PhotoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_photo】的数据库操作Mapper
* @createDate 2023-10-14 11:21:06
* @Entity com.moon.entity.Photo
*/
public interface PhotoMapper extends BaseMapper<Photo> {

    /**
     * 查询照片列表
     *
     * @param albumId 相册id
     * @return 后台照片列表
     */
    List<PhotoVo> selectPhotoList(@Param("albumId") Integer albumId);

    /**
     * 查询后台照片列表
     *
     * @param limit   页码
     * @param size    大小
     * @param albumId 相册id
     * @return 后台照片列表
     */
    List<PhotoBackVo> selectPhotoBackVOList(@Param("limit") Long limit, @Param("size") Long size, @Param("albumId") Integer albumId);
}




