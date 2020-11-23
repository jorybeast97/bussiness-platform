package com.fanhao.businessplatform.utils;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    //默认Cookie生命周期，7天
    public static final int DEFAULT_COOKIE_EXPIRE_TIME = 7 * 24 * 60 * 60;
    public static final String BAIDU_API_AK = "ZGaZoGpHnCQxRMISfbYhxMm4GDRva1dp";
    public static final String BAIDU_API_PREFIX = "http://api.map.baidu.com/location/ip?ak=";

    //用于发送Http请求
    private static RestTemplate restTemplate = new RestTemplate();

    /**
     * 直接获取ip归属地
     * @param ip
     * @return
     */
    public static String getAddressResult(final String ip) {
        Map<String, String> map = getIpAddressInformation(ip);
        if (map != null) {
            return map.get("province") + map.get("city");
        }
        return "局域网或本机";
    }

    /**
     * 根据IP地址获取其实际地理位置
     * @param ip
     * @return
     */
    public static Map<String, String> getIpAddressInformation(final String ip) {
        if (StringUtils.isEmpty(ip)) return null;
        String addressJson = null;
        addressJson = restTemplate.postForObject(getBaiduApiUrl(ip), null, String.class);
        Gson gson = GsonUtils.getGson();
        Map initMap = gson.fromJson(addressJson, Map.class);
        String status = String.valueOf(initMap.get("status"));
        Map<String, String> addressMap = new HashMap<>();
        if (status.equals("1.0")){
            LOGGER.warn("getIpAddressInformation - 无法查明请求来源IP所属:" + ip);
            return null;
        }else {
            Map addressDetail = null;
            Map point = null;
            try {
                Map content = (Map) initMap.get("content");
                addressDetail = (Map) content.get("address_detail");
                point = (Map) content.get("point");
            } catch (Exception e) {
                return null;
            }
            addressMap.put("province", (String) addressDetail.get("province"));
            addressMap.put("city", (String) addressDetail.get("city"));
            addressMap.put("x", (String) point.get("x"));
            addressMap.put("y", (String) point.get("y"));
        }
        return addressMap;
    }

    /**
     * 获取百度地图对应的API
     * @param ip
     * @return
     */
    private static String getBaiduApiUrl(final String ip) {
        String url = BAIDU_API_PREFIX + BAIDU_API_AK + "&ip=" + ip + "&coor=bd09ll";
        return url;
    }

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

    public static String getLocalHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getIpAddress(final HttpServletRequest request) {
        return request.getRemoteAddr();
    }
}
