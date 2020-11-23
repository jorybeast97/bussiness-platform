package com.fanhao.businessplatform.utils;

import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.common.constant.ResultStatus;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 有关权限相关操作
 * @author fanhao
 */
@Component
public class PermissionUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionUtils.class);
    public static final String TOKEN = "jwt-token";
    public static final long DEFAULT_TOKEN_EXPIRE_TIME = 24 * 60 * 60 * 1000;
    public static final String JWT_TOKEN_ID = "id";
    public static final String JWT_TOKEN_USERNAME = "username";
    public static final String JWT_TOKEN_ROLE = "role";
    public static final String JWT_TOKEN_NAME = "name";
    public static final String JWT_TOKEN_EXPIRE_TIME = "expire_time";

    private static String secretKey;

    private static String subject;

    private static JwtBuilder jwtBuilder;

    /**
     * 生成JWT Token，可添加补充信息
     * @param username
     * @param role
     * @param expireTime
     * @param additionalInfo
     * @return
     */
    public static String generateJWT(String username,
                                     String role,
                                     String name,
                                     Long expireTime,Map<String,Object> additionalInfo) {
        long expire = expireTime == null ? DEFAULT_TOKEN_EXPIRE_TIME : expireTime;
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(role)) return null;
        Map<String, Object> payload = new HashMap<>();
        payload.put(JWT_TOKEN_USERNAME, username);
        payload.put(JWT_TOKEN_ROLE, role);
        payload.put(JWT_TOKEN_NAME, name);
        jwtBuilder = newInstanceBuilder();
        if (additionalInfo != null) jwtBuilder.setClaims(additionalInfo);
        String token = jwtBuilder
                .setSubject(subject)
                .setClaims(payload)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return token;
    }

    /**
     * 解析 token
     * @param token
     * @return
     */
    private static Claims check(String token) {
        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        } catch (Exception e) {
            LOGGER.warn("PermissionUtils - check() : Token解析失败，Token : {" + token + "}");
        }
        return null;
    }

    /**
     * 检查Token是否有效
     * @param role
     * @param username
     * @param token
     * @return
     */
    public static CommonResult<Claims> checkPermissionInfo(final String role,
                                                           final String username,
                                                           final String token) {
        Claims claims = check(token);
        CommonResult<Claims> commonResult = new CommonResult<>();
        if (claims != null &&
                claims.get(JWT_TOKEN_USERNAME, String.class).equals(username) &&
                claims.get(JWT_TOKEN_ROLE, String.class).equals(role) &&
                claims.getExpiration().after(new Date())) {
            commonResult.setCode(ResultStatus.SUCCESS.getResultCode());
            commonResult.setData(claims);
            commonResult.setMessage(null);
            return commonResult;
        }
        LOGGER.warn("PermissionUtils - checkPermissionInfo() : Check permission is failed!");
        commonResult.setCode(ResultStatus.NO_PERMISSION.getResultCode());
        commonResult.setData(claims);
        commonResult.setMessage("权限校验失败,请重新登录");
        return commonResult;
    }

    /**
     * 获取token中相关信息
     * @param token
     * @return
     */
    public static Map<String, String> getClaimsInformation(final String token) {
        HashMap<String, String> information = new HashMap<>();
        Claims claims = check(token);
        if (claims != null) {
            information.put(JWT_TOKEN_USERNAME, claims.get(JWT_TOKEN_USERNAME, String.class));
            information.put(JWT_TOKEN_ROLE, claims.get(JWT_TOKEN_ROLE, String.class));
            information.put(JWT_TOKEN_EXPIRE_TIME, String.valueOf(claims.getExpiration().getTime()));
            information.put(JWT_TOKEN_NAME, claims.get(JWT_TOKEN_NAME, String.class));
        }
        return information;
    }

    private static JwtBuilder newInstanceBuilder() {
        if (jwtBuilder == null) {
            jwtBuilder = new DefaultJwtBuilder();
        }
        return jwtBuilder;
    }

    //将配置文件读取到静态变量
    @Value("${permission.secret-key}")
    private void setSecretKey(String secretKey) {
        PermissionUtils.secretKey = secretKey;
    }

    @Value("${permission.subject}")
    private void setSubject(String subject) {
        PermissionUtils.subject = subject;
    }
}
