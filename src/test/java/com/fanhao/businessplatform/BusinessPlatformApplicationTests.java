package com.fanhao.businessplatform;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class BusinessPlatformApplicationTests {


    @Autowired
    private EmployeeService employeeService;

    @Test
    void contextLoads() {

    }

    @Test
    void createEmployee() {
        for (int i = 0; i < 100; i++) {
            Employee employee = new Employee();
            String email = String.valueOf(RandomUtil.randomLong(0, 999999999)) + "@gmail.com";
            String name = randomName();
            String username = randomName();
            String password = UUID.randomUUID().toString();
            String address = randomName() + "地址";
            boolean gender = true;
            employee.setUsername(username);
            employee.setPassword(password);
            employee.setName(name);
            employee.setAddress(address);
            employee.setGender(gender);
            employee.setPhone(String.valueOf(RandomUtil.randomLong(10000000000L,19000000000L)));
            employee.setEmail(email);
            employee.setDepartment(RandomUtil.randomInt(1, 6));
            employee.setPosition("员工");
            employee.setRole("admin");
            employee.setBirthday(new Date());
            employee.setIdCard(UUID.randomUUID().toString());
            employee.setSchool("华北理工大学");
            employee.setContractStartDate(new Date());
            employee.setQuitDate(new Date());
            employee.setWorkAge(RandomUtil.randomInt(1, 10));
            employeeService.addEmployee(employee);
        }
    }

    public String randomName() {
        String firstName = "";
        for (int i = 0; i < 10; i++) {
            firstName = firstName + String.valueOf(RandomUtil.randomChar());
        }
        return firstName;
    }

}
