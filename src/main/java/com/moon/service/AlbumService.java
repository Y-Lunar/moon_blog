package com.moon.service;

import com.moon.entity.Album;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.entity.dto.AlbumDto;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.vo.AlbumBackVo;
import com.moon.entity.vo.AlbumVo;
import com.moon.entity.vo.PageResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_album】的数据库操作Service
* @createDate 2023-09-21 16:16:51
*/
public interface AlbumService extends IService<Album> {

    /**
     * 查看相册列表
     *
     * @return
     */
    List<AlbumVo> AlbumVoList();

    /**
     * 查看后台相册列表
     *
     * @param condition 条件
     * @return
     */
    PageResult<AlbumBackVo> albumBackVoList(ConditionDto condition);

    /**
     * 上传相册封面
     *
     * @param file 文件
     * @return 相册封面地址
     */
    String uploadAlbumCover(MultipartFile file);

    /**
     * 添加相册
     *
     * @param album 相册信息
     * @return
     */
    void addAlbum(AlbumDto album);

    /**
     * 删除相册
     *
     * @param albumId 相册id
     * @return
     */
    void deleteAlbum(Integer albumId);

    /**
     * 修改相册
     *
     * @param album 相册信息
     * @return
     */
    void updateAlbum(AlbumDto album);

    /**
     * 编辑相册
     *
     * @param albumId 相册id
     * @return 相册
     */
    AlbumDto editAlbum(Integer albumId);
}
