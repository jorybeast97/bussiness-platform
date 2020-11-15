package com.fanhao.businessplatform.controller;

import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.common.constant.ResultStatus;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String employeePage() {
        return "/employee/employeelist";
    }

    @RequestMapping(value = "/freemark")
    public String freemark() {
        return "freemark";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<Employee>> employeeList(final HttpServletRequest request,
                                     final HttpServletResponse response,
                                     final Integer page,
                                     final Integer limit) {
        List<Employee> employeeList = employeeService.selectAllEmployee(page, limit);
        CommonResult<List<Employee>> commonResult = new CommonResult<>();
        commonResult.setCode("200");
        commonResult.setData(employeeList);
        commonResult.setMessage("测试消息");
        commonResult.setCount(Long.valueOf(employeeList.size()));
        return commonResult;
    }
}
