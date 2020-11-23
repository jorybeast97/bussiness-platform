package com.fanhao.businessplatform;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.service.ConsoleService;
import com.fanhao.businessplatform.service.TaskService;
import com.fanhao.businessplatform.utils.*;
import com.fanhao.businessplatform.service.EmployeeService;
import com.google.gson.Gson;
import org.apache.http.client.HttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
class BusinessPlatformApplicationTests {


    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DataCreaterUtils dataCreaterUtils;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ConsoleService consoleService;

    @Test
    void contextLoads() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoibm9ybWFsIiwibmFtZSI6IuaZrumAmua1i-ivlei0puWPtyIsImV4cCI6MTYwNjIwMjk0OCwiaWF0IjoxNjA2MTE2NTQ4LCJ1c2VybmFtZSI6InRlc3QifQ.6aSNHczZKxouma8DAd8H0MWtYMJZ37ow1JKwzj8iDeE";
        String username = PermissionUtils.getClaimsInformation(token).get(PermissionUtils.JWT_TOKEN_USERNAME);
        String name = PermissionUtils.getClaimsInformation(token).get(PermissionUtils.JWT_TOKEN_NAME);
        String role = PermissionUtils.getClaimsInformation(token).get(PermissionUtils.JWT_TOKEN_ROLE);
        System.out.println(username + name + role);
    }

}
