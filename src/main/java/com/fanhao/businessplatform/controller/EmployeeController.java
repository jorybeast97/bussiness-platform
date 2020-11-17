package com.fanhao.businessplatform.controller;

import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.entity.BO.EmployeeBO;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller("employee")
@RequestMapping(value = "/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String employeePage() {
        return "/employee/employeelist";
    }

    @RequestMapping(value = "/addemployee")
    public String addEmployeePage() {
        return "/employee/addEmployee";
    }

    @RequestMapping(value = "/editemployee")
    public String editEmployeePage(Integer id,
                                   Model model) {
        Employee employee = employeeService.selectEmployeeById(id);
        model.addAttribute("employee_info", employee);
        return "/employee/editEmployee";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<EmployeeBO>> employeeList(final HttpServletRequest request,
                                                       final HttpServletResponse response,
                                                       final Integer page,
                                                       final Integer limit) {

        return employeeService.selectList(page, limit);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<String> save(Integer id, String username, String password,
                                     String name, String address, Boolean gender,
                                     String phone, String email, Integer department,
                                     String position, String role, String birthday,
                                     String idCard, String school, String contractStartDate,
                                     String quitDate, Integer workAge, Boolean status,
                                     String remark) {
        return employeeService.addOrUpdateEmployeeByArgs(id, username, password, name, address, gender, phone, email, department, position, role, birthday, idCard, school, contractStartDate, quitDate, workAge, status, remark);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<String> delete(Integer id) {
        return employeeService.deleteEmployee(id);
    }

}
