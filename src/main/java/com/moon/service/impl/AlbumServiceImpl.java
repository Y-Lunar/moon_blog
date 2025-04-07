package com.moon.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.entity.Album;
import com.moon.entity.BlogFile;
import com.moon.entity.Photo;
import com.moon.entity.dto.AlbumDto;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.vo.AlbumBackVo;
import com.moon.entity.vo.AlbumVo;
import com.moon.entity.vo.PageResult;
import com.moon.enums.FilePathEnum;
import com.moon.mapper.BlogFileMapper;
import com.moon.mapper.PhotoMapper;
import com.moon.service.AlbumService;
import com.moon.mapper.AlbumMapper;
import com.moon.strategy.context.UploadStrategyContext;
import com.moon.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.moon.constant.CommonConstant.FALSE;
import static com.moon.enums.FilePathEnum.PHOTO;

/**
* @author Y.0
* @description 针对表【t_album】的数据库操作Service实现
* @createDate 2023-09-21 16:16:51
*/
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album>
    implements AlbumService{

    @Autowired(required = false)
    private AlbumMapper albumMapper;

    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    @Autowired(required = false)
    private BlogFileMapper blogFileMapper;

    @Autowired(required = false)
    private PhotoMapper photoMapper;

    /**
     * 查看相册列表
     *
     * @return
     */
    @Override
    public List<AlbumVo> AlbumVoList() {
        List<Album> albums = albumMapper.selectList(
                new LambdaQueryWrapper<Album>()
                        .select(Album::getId,
                                Album::getAlbumName,
                                Album::getAlbumDesc,
                                Album::getAlbumCover));
        List<AlbumVo> albumVos = BeanCopyUtils.copyBeanList(albums, AlbumVo.class);
        return albumVos;
    }

    /**
     * 查看后台相册列表
     *
     * @param condition 条件
     * @return
     */
    @Override
    public PageResult<AlbumBackVo> albumBackVoList(ConditionDto condition) {
        // 查询相册数量
        Long count = albumMapper.selectCount(new LambdaQueryWrapper<Album>()
                .like(StringUtils.hasText(condition.getKeyword()), Album::getAlbumName, condition.getKeyword()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询相册信息
        List<AlbumBackVo> albumList = albumMapper.selectAlbumBackVO(PageUtils.getLimit(), PageUtils.getSize(), condition.getKeyword());
        return new PageResult<>(albumList, count);
    }

    /**
     * 上传相册封面
     *
     * @param file 文件
     * @return 相册封面地址
     */
    @Override
    public String uploadAlbumCover(MultipartFile file) {
        // 上传文件
        String url = uploadStrategyContext.executeUploadStrategy(file, PHOTO.getPath());
        try {
            // 获取文件md5值
            String md5 = FileUtils.getMd5(file.getInputStream());
            // 获取文件扩展名
            String extName = FileUtils.getExtension(file);
            BlogFile existFile = blogFileMapper.selectOne(new LambdaQueryWrapper<BlogFile>()
                    .select(BlogFile::getId)
                    .eq(BlogFile::getFileName, md5)
                    .eq(BlogFile::getFilePath, PHOTO.getFilePath()));
            if (Objects.isNull(existFile)) {
                // 保存文件信息
                BlogFile newFile = BlogFile.builder()
                        .fileUrl(url)
                        .fileName(md5)
                        .filePath(PHOTO.getFilePath())
                        .extendName(extName)
                        .fileSize((int) file.getSize())
                        .isDir(FALSE)
                        .build();
                blogFileMapper.insert(newFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 添加相册
     *
     * @param album 相册信息
     * @return
     */
    @Override
    public void addAlbum(AlbumDto album) {
        // 相册是否存在
        Album existAlbum = albumMapper.selectOne(new LambdaQueryWrapper<Album>()
                .select(Album::getId)
                .eq(Album::getAlbumName, album.getAlbumName()));
        Assert.isNull(existAlbum, album.getAlbumName() + "相册已存在");
        // 添加新相册
        Album newAlbum = BeanCopyUtils.copyBean(album, Album.class);
        baseMapper.insert(newAlbum);
    }

    /**
     * 删除相册
     *
     * @param albumId 相册id
     * @return
     */
    @Override
    public void deleteAlbum(Integer albumId) {
        // 查询照片数量
        Long count = photoMapper.selectCount(new LambdaQueryWrapper<Photo>()
                .eq(Photo::getAlbumId, albumId));
        Assert.isFalse(count > 0, "相册下存在照片");
        // 不存在照片则删除
        albumMapper.deleteById(albumId);
    }

    /**
     * 修改相册
     *
     * @param album 相册信息
     * @return
     */
    @Override
    public void updateAlbum(AlbumDto album) {
        // 相册是否存在
        Album existAlbum = albumMapper.selectOne(new LambdaQueryWrapper<Album>()
                .select(Album::getId)
                .eq(Album::getAlbumName, album.getAlbumName()));
        Assert.isFalse(Objects.nonNull(existAlbum) && !existAlbum.getId().equals(album.getId()),
                album.getAlbumName() + "相册已存在");
        // 修改相册
        Album newAlbum = BeanCopyUtils.copyBean(album, Album.class);
        baseMapper.updateById(newAlbum);
    }

    /**
     * 编辑相册
     *
     * @param albumId 相册id
     * @return 相册
     */
    @Override
    public AlbumDto editAlbum(Integer albumId) {
        Album existAlbum = albumMapper.selectOne(new LambdaQueryWrapper<Album>()
                .select(Album::getId,
                        Album::getAlbumName,
                        Album::getAlbumDesc,
                        Album::getAlbumCover)
                .eq(Album::getId, albumId));
        AlbumDto albumDto = BeanCopyUtils.copyBean(existAlbum, AlbumDto.class);
        return albumDto;
    }
}




