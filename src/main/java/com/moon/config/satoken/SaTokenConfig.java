package com.moon.config.satoken;


import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.json.JSONUtil;
import com.moon.interceptor.PageableInterceptor;
import com.moon.utils.Result;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import static com.moon.enums.StatusCodeEnum.UNAUTHORIZED;
/**
 * SaToken配置
 *
 * @author:Y.0
 * @date:2023/9/21
 */
@Component
public class SaTokenConfig implements WebMvcConfigurer {

    private final String[] EXCLUDE_PATH_PATTERNS = {
            "/swagger-resources",
            "/webjars/**",
            "/v2/api-docs",
            "/doc.html",
            "/favicon.ico",
            "/login",
    };

    private final long timeout = 600;

    /**
     * 注册 Sa-Token 拦截器，打开注解式鉴权功能
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册分页拦截器
        registry.addInterceptor(new PageableInterceptor());
        // 注册 Sa-Token 的注解拦截器，打开注解式鉴权功能
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public SaServletFilter getsaServletFilter(){
        return new SaServletFilter()
                //拦截路径
                .addExclude("/**")
                //排除路径
                .addExclude(EXCLUDE_PATH_PATTERNS)
                // 前置函数：在每次认证函数之前执行（BeforeAuth 不受 includeList 与 excludeList 的限制，所有请求都会进入）
                .setBeforeAuth(obj -> {
                    SaHolder.getResponse()
                            // 允许指定域访问跨域资源
                            .setHeader("Access-Control-Allow-Origin", "*")
                            // 允许所有请求方式
                            .setHeader("Access-Control-Allow-Methods", "*")
                            // 有效时间
                            .setHeader("Access-Control-Max-Age", "3600")
                            // 允许的header参数
                            .setHeader("Access-Control-Allow-Headers", "*");
                    // 如果是预检请求，则立即返回到前端
                    SaRouter.match(SaHttpMethod.OPTIONS)
                            .free(r -> System.out.println("--------OPTIONS预检请求，不做处理"))
                            .back();
                })
                // 认证函数: 每次请求执行
                .setAuth(obj -> {
                    System.out.println("---------- 进入Sa-Token全局认证 -----------");
                    // 检查是否登录
                    SaRouter.match("/admin/**").check(r -> StpUtil.checkLogin());
                    // 刷新token有效期
                    if (StpUtil.getTokenTimeout() < timeout) {
                        StpUtil.renewTimeout(1800);
                    }
                    // 输出 API 请求日志，方便调试代码
//                    SaManager.getLog().debug("----- 请求path={}  提交token={}", SaHolder.getRequest().getRequestPath(), StpUtil.getTokenValue());
                })
                //  异常处理函数：每次认证函数发生异常时执行此函数
                .setError(e -> {
                    System.out.println("---------- 进入Sa-Token异常处理 -----------");
                    // 设置响应头
                    SaHolder.getResponse().setHeader("Content-Type", "application/json;charset=UTF-8");
                    if (e instanceof NotLoginException) {
                        return JSONUtil.toJsonStr(Result.error(UNAUTHORIZED.getCode(), UNAUTHORIZED.getMsg()));
                    }
                    return SaResult.error(e.getMessage());
                });
    }
}
