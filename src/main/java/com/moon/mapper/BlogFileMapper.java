package com.moon.mapper;

import com.moon.entity.BlogFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moon.entity.vo.FileVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_blog_file】的数据库操作Mapper
* @createDate 2023-10-15 17:28:24
* @Entity com.moon.entity.BlogFile
*/
public interface BlogFileMapper extends BaseMapper<BlogFile> {

    /**
     * 查询后台文件列表
     *
     * @param limit    页码
     * @param size     大小
     * @param filePath 文件路径
     * @return 后台文件列表
     */
    List<FileVO> selectFileVOList(@Param("limit") Long limit, @Param("size") Long size, @Param("filePath") String filePath);
}




