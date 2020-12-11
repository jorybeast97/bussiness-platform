package com.fanhao.businessplatform.config;

import com.fanhao.businessplatform.cache.CacheOperation;
import com.fanhao.businessplatform.utils.HttpUtils;
import com.fanhao.businessplatform.utils.PermissionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 全局拦截器
 */
@Service("pageInterceptor")
public class PageInterceptor implements HandlerInterceptor {
    private Logger LOGGER = LoggerFactory.getLogger(PageInterceptor.class);

    @Autowired
    private CacheOperation cacheOperation;

    /**
     * 全局拦截器,当在Cookie中检测不到有效Cookie时，重定向到登录页面
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String token = HttpUtils.getCookie(request, PermissionUtils.TOKEN);
        if (token == null) {
            response.sendRedirect("/login");
            return false;
        }
        cacheOperation.recordUserOperation(request, response, token);
        return true;
    }
}
