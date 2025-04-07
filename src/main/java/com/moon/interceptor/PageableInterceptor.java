package com.moon.interceptor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moon.utils.PageUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import static com.moon.constant.PageConstant.*;

/**
 * 分页拦截器
 * 请求处理之前和请求处理完成后，根据请求中的参数设置当前分页信息，以便在处理请求的控制器（Controller）方法中使用。
 * @author:Y.0
 * @date:2023/9/21
 */
public class PageableInterceptor implements HandlerInterceptor {

    //在请求被处理之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        String currentPage = request.getParameter(CURRENT);
        String pageSize = Optional.ofNullable(request.getParameter(SIZE)).orElse(DEFAULT_SIZE);
        if (StringUtils.hasText(currentPage)) {
            PageUtils.setCurrentPage(new Page<>(Long.parseLong(currentPage), Long.parseLong(pageSize)));
        }
        return true;
    }

    //在请求完成之后执行。
    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        PageUtils.remove();
    }

}
