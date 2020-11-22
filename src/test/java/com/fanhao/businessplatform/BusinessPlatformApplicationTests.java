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
        List<DateTime> year = consoleService.getPreMonthDateList();
        List<Double> salary = consoleService.getEveryMonthSalaryInfo(year);
        System.out.println(consoleService.getSalaryIndexList(year));

    }

}
