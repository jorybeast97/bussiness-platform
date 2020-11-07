package com.fanhao.businessplatform;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.common.constant.ResultStatus;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.mapper.EmployeeMapper;
import com.fanhao.businessplatform.service.EmployeeService;
import com.fanhao.businessplatform.service.SalaryService;
import com.fanhao.businessplatform.utils.PermissionUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Permission;
import java.util.Date;
import java.util.List;

@SpringBootTest
class BusinessPlatformApplicationTests {

    @Autowired
    private SalaryService service;

    @Test
    void contextLoads() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiYWRtaW4iLCJleHAiOjE2MDQ3NTU4OTYsImlhdCI6MTYwNDY2OTQ5NiwidXNlcm5hbWUiOiJmYW5oYW8xIn0.3o6o7W1KsGpTuZoadFbsZynl6oQBSWXw93QnsWdtf1w";
        System.out.println("Token内容 : " + token);
        CommonResult<Claims> claimsCommonResult = PermissionUtils.
                checkPermissionInfo("admin", "fanhao1", token);
        if (!claimsCommonResult.getResultCode().equals(ResultStatus.NO_PERMISSION.getResultCode())) {
            System.out.println(claimsCommonResult.getData().get("username", String.class));
            System.out.println(claimsCommonResult.getData().get("role", String.class));
        }else {
            System.out.println(claimsCommonResult.getAttachMessage());
        }
    }

    @Test
    void selectPageTest() {
        System.out.println(service.selectSalaryById(1));
    }
}
