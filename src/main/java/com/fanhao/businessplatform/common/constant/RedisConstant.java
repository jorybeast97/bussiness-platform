package com.fanhao.businessplatform.common.constant;

/**
 * 用于记录常用Redis中常量
 */
public class RedisConstant {
    //用户在线操作前缀
    public static final String USER_ONLINE_OPERATION_PREFIX = "userOnlineOperation:";
    //用户默认在线时间，300秒后认为该用户下线
    public static final Long USER_ONLINE_OPERATION_EXPIRE_TIME = 300L;
}
