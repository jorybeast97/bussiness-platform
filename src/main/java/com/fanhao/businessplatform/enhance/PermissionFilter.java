package com.fanhao.businessplatform.enhance;

import com.fanhao.businessplatform.enhance.annotation.PermissionVerification;
import com.fanhao.businessplatform.utils.HttpUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 该类为权限拦截类，主要负责对敏感服务进行权限校验
 * @author 范昊
 */
@Component
@Aspect
public class PermissionFilter {
    private static final Logger LOG = LoggerFactory.getLogger(PermissionFilter.class);

    @Autowired
    private HttpServletRequest httpServletRequest;

    //获取注解切点
    @Pointcut("@annotation(com.fanhao.businessplatform.enhance.annotation.PermissionVerification)")
    private void getPermissionAnnotationPoint() {

    }

    @Before("getPermissionAnnotationPoint() && @annotation(annotation)")
    private void checkPermission(PermissionVerification annotation) {
        String token = HttpUtils.getCookie(httpServletRequest, HttpUtils.TOKEN);
        if (StringUtils.isEmpty(token)) System.out.println("未登录");
        else if (!token.equals(annotation.role())) System.out.println("权限不足");
        else System.out.println("访问成功");
    }
}
