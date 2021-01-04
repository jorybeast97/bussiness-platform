package com.fanhao.businessplatform.cache;

import com.fanhao.businessplatform.common.constant.RedisConstant;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.service.EmployeeService;
import com.fanhao.businessplatform.utils.PermissionUtils;
import com.fanhao.businessplatform.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author fanhao
 *
 * 该类为Redis Cache相关操作集合，Redis在本系统中，主要用作缓存对一些性能问题进行改善，
 * 如果机器上没有部署Redis，请关闭applicaiton.yml中enable-cache选项，防止异常报错
 */
@Component
public class CacheOperation {
    private final Logger LOGGER = LoggerFactory.getLogger(CacheOperation.class);

    @Autowired
    private StringRedisTemplate redisTemplate = new StringRedisTemplate();

    @Autowired
    private EmployeeService employeeService;

    @Value("${spring.redis.enable-cache}")
    private boolean enableCache;

    /**
     * 用户进行操作时，记录用户在线信息
     * @param request
     * @param response
     * @param token
     */
    public void recordUserOperation(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final String token) {
        if (!enableCache) return;
        final String username = PermissionUtils.getClaimsInformation(token).get(PermissionUtils.JWT_TOKEN_USERNAME);
        final String redisKey = getUserOnlineOperationKey(username);
        try {
            redisTemplate.opsForValue().set(redisKey, username, RedisConstant.USER_ONLINE_OPERATION_EXPIRE_TIME, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前在线用户
     * @return
     */
    public List<Employee> getOnlineUserList() {
        Set<String> usernames = null;
        try {
            usernames = redisTemplate.keys(RedisConstant.USER_ONLINE_OPERATION_PREFIX + "*");
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Employee> employeeList = new ArrayList<>();
        usernames.forEach(usernameKey -> {
            String username = usernameKey.split(RedisConstant.USER_ONLINE_OPERATION_PREFIX)[1];
            Employee employee = employeeService.selectEmployeeByUsername(username);
            employeeList.add(employee);
        });
        return employeeList;
    }

    /**
     * 获取用户在线操作对应的RedisKey
     * @param username
     * @return
     */
    private String getUserOnlineOperationKey(final String username) {
        if (StringUtils.isEmpty(username)){
            return null;
        }
        //拼接为对应的Key
        String userOnlineOperationKey = RedisConstant.USER_ONLINE_OPERATION_PREFIX + username;
        return userOnlineOperationKey;
    }
}
