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
