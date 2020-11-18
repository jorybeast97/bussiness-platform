package com.fanhao.businessplatform;

import cn.hutool.core.util.RandomUtil;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.utils.DataCreaterUtils;
import com.fanhao.businessplatform.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.UUID;

@SpringBootTest
class BusinessPlatformApplicationTests {


    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DataCreaterUtils dataCreaterUtils;

    @Test
    void contextLoads() {
        for (int i = 0; i < 150; i++) {
            dataCreaterUtils.create();
        }
    }

}
