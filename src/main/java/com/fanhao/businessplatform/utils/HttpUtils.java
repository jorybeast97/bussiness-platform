package com.fanhao.businessplatform.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class HttpUtils {
    public static final String TOKEN = "jwt-token";
    //默认Cookie生命周期，7天
    public static final int DEFAULT_COOKIE_EXPIRE_TIME = 7 * 24 * 60 * 60;

    /**
     * 获取Cookie中对应的信息
     * @param request
     * @param args
     * @return
     */
    public static String getCookie(HttpServletRequest request, String args) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(args)) return c.getValue();
            }
        }
        return null;
    }

    /**
     * 写入Cookie相关信息
     * @param response
     * @param cookieName
     * @param value
     * @param expireTime
     */
    public static void writeCookie(final HttpServletResponse response,
                                   final String cookieName,
                                   final String value,
                                   final Integer expireTime) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setPath("/");
        if (expireTime == null) cookie.setMaxAge(DEFAULT_COOKIE_EXPIRE_TIME);
        else cookie.setMaxAge(expireTime);
        response.addCookie(cookie);
    }
}
