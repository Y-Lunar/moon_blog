package com.moon.mapper;

import com.moon.entity.Album;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moon.entity.dto.AlbumDto;
import com.moon.entity.vo.AlbumBackVo;
import com.moon.entity.vo.AlbumVo;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_album】的数据库操作Mapper
* @createDate 2023-09-21 16:16:51
* @Entity com.moon.entity.Album
*/
public interface AlbumMapper extends BaseMapper<Album> {


    /**
     * 查询后台相册列表
     *
     * @param limit   页码
     * @param size    大小
     * @param keyword 关键字
     * @return 后台相册列表
     */
    List<AlbumBackVo> selectAlbumBackVO(Long limit, Long size, String keyword);

    /**
     * 编辑相册
     *
     * @param albumId 相册id
     * @return 相册
     */
    AlbumDto selectAlbumById(Integer albumId);

    /**
     * 根据id查询照片相册信息
     *
     * @param albumId 相册id
     * @return 照片相册信息
     */
    AlbumBackVo selectAlbumInfoById(Integer albumId);
}




