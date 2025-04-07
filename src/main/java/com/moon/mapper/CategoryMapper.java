package com.moon.mapper;

import com.moon.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moon.entity.vo.CategoryAdminVo;
import com.moon.entity.vo.CategoryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_category】的数据库操作Mapper
* @createDate 2023-09-23 09:56:29
* @Entity com.moon.entity.Category
*/
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 查询分类列表
     *
     * @return 分类列表
     */
    List<CategoryVo> selectCategoryVO();


    /**
     * 查询后台分类列表
     *
     * @param limit   页码
     * @param size    大小
     * @param keyword 关键字
     * @return 后台分类列表
     */
    List<CategoryAdminVo> selectCategoryBackVO(@Param("limit") Long limit, @Param("size") Long size, @Param("keyword") String keyword);
}




