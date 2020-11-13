package com.fanhao.businessplatform;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.common.constant.ResultStatus;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.mapper.EmployeeMapper;
import com.fanhao.businessplatform.service.EmployeeService;
import com.fanhao.businessplatform.service.SalaryService;
import com.fanhao.businessplatform.utils.PermissionUtils;
import com.fanhao.businessplatform.utils.ThreadUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class BusinessPlatformApplicationTests {


    @Test
    void contextLoads() {
        ThreadUtils.getThreadPoolHelper().getInformation();
    }

}
