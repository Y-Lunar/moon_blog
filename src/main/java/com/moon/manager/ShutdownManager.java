package com.moon.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 *
 *
 * @author:Y.0
 * @date:2023/10/9
 */
@Component
public class ShutdownManager {
    private static final Logger logger = LoggerFactory.getLogger(ShutdownManager.class);

    @PreDestroy
    public void destroy() {
        shutdownAsyncManager();
    }

    /**
     * 停止异步执行任务
     */
    private void shutdownAsyncManager()
    {
        try
        {
            logger.info("(๑‾᷅^‾᷅๑)====关闭后台任务任务线程池====(ಡωಡ)");
            logger.info("(♥◠‿◠)ﾉﾞ  关闭后台服务成功   ლ(´ڡ`ლ)ﾞ");
            AsyncManager.getInstance().shutdown();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }
}
