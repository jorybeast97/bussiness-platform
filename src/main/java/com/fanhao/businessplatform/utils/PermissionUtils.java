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

import javax.xml.bind.DatatypeConverter;
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
    //默认Token过期时间
    private static final long DEFAULT_TOKEN_EXPIRE_TIME = 24 * 60 * 60 * 1000;

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
        payload.put("username", username);
        payload.put("role", role);
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
                claims.get("username", String.class).equals(username) &&
                claims.get("role", String.class).equals(role) &&
                claims.getExpiration().after(new Date())) {
            commonResult.setResultCode(ResultStatus.SUCCESS.getResultCode());
            commonResult.setData(claims);
            commonResult.setAttachMessage(null);
            return commonResult;
        }
        LOGGER.warn("PermissionUtils - checkPermissionInfo() : Check permission is failed!");
        commonResult.setResultCode(ResultStatus.NO_PERMISSION.getResultCode());
        commonResult.setData(claims);
        commonResult.setAttachMessage("权限校验失败,请重新登录");
        return commonResult;
    }

    private static JwtBuilder newInstanceBuilder() {
        if (jwtBuilder == null) {
            jwtBuilder = new DefaultJwtBuilder();
        }
        return jwtBuilder;
    }

    @Value("${permission.secret-key}")
    private void setSecretKey(String secretKey) {
        PermissionUtils.secretKey = secretKey;
    }

    @Value("${permission.subject}")
    private void setSubject(String subject) {
        PermissionUtils.subject = subject;
    }
}
