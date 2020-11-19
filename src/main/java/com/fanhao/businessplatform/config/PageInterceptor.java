package com.fanhao.businessplatform.config;

import com.fanhao.businessplatform.utils.HttpUtils;
import com.fanhao.businessplatform.utils.PermissionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Service("pageInterceptor")
public class PageInterceptor implements HandlerInterceptor {

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
        Map<String, String> tokenInfo = PermissionUtils.getClaimsInformation(token);
        return true;
    }
}
