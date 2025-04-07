package com.moon.strategy;

/**
 * 点赞策略
 *
 * @author:Y.0
 * @date:2023/10/23
 */
public interface LikeStrategy {

    /**
     * 点赞
     *
     * @param typeId 类型id
     */
    void like(Integer typeId);

}
