package com.moon.interceptor;

import com.alibaba.fastjson2.JSON;
import com.moon.annotation.AccessLimit;
import com.moon.service.RedisService;
import com.moon.utils.IpUtils;
import com.moon.utils.Result;
import com.moon.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * redis拦截器
 *
 * @author:Y.0
 * @date:2023/11/8
 */
@Slf4j
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {

    @Autowired(required = false)
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        boolean result = true;
        // Handler 是否为 HandlerMethod 实例
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            //方法上没有访问控制的注解，直接通过
            if (accessLimit != null) {
                int seconds = accessLimit.seconds();
                int maxCount = accessLimit.maxCount();
                String ip = IpUtils.getIpAddress(request);
                String method = request.getMethod();
                String requestUri = request.getRequestURI();
                String redisKey = ip + ":" + method + ":" + requestUri;
                try {
                    Long count = redisService.incr(redisKey, 1L);
                    // 第一次访问
                    if (Objects.nonNull(count) && count == 1) {
                        redisService.setExpire(redisKey, seconds, TimeUnit.SECONDS);
                    } else if (count > maxCount) {
                        WebUtils.renderString(response, JSON.toJSONString(Result.error(accessLimit.msg())));
                        log.warn(redisKey + "请求次数超过每" + seconds + "秒" + maxCount + "次");
                        result = false;
                    }
                } catch (RedisConnectionFailureException e) {
                    log.error("redis错误: " + e.getMessage());
                    result = false;
                }
            }
        }
        return result;
    }

}
