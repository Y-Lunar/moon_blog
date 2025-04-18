package com.moon.strategy.context;

import com.moon.enums.LikeTypeEnum;
import com.moon.strategy.LikeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 点赞策略上下文
 *
 * @author:Y.0
 * @date:2023/10/23
 */
@Service
public class LikeStrategyContext {

    @Autowired(required = false)
    private Map<String, LikeStrategy> likeStrategyMap;


    /**
     * 点赞
     *
     * @param likeType 点赞类型
     * @param typeId   类型id
     */
    public void executeLikeStrategy(LikeTypeEnum likeType, Integer typeId) {
        likeStrategyMap.get(likeType.getStrategy()).like(typeId);
    }
}
