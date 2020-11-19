package com.fanhao.businessplatform;

import cn.hutool.core.util.RandomUtil;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.utils.DataCreaterUtils;
import com.fanhao.businessplatform.service.EmployeeService;
import com.fanhao.businessplatform.utils.GsonUtils;
import com.fanhao.businessplatform.utils.HttpUtils;
import com.google.gson.Gson;
import org.apache.http.client.HttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
class BusinessPlatformApplicationTests {


    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DataCreaterUtils dataCreaterUtils;

    @Test
    void contextLoads() {
        Map<String, String> addressMap = HttpUtils.getIpAddressInformation("60.2.111.28");
        System.out.println(addressMap.get("province") + addressMap.get("city"));
    }

}
