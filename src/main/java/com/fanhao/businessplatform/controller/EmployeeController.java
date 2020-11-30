package com.fanhao.businessplatform.controller;

import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.entity.BO.EmployeeBO;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.service.EmployeeService;
import com.fanhao.businessplatform.service.PermissionService;
import com.fanhao.businessplatform.utils.PermissionUtils;
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

    @Autowired
    private PermissionService permissionService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String employeePage(final HttpServletRequest request,
                               final HttpServletResponse response,
                               Model model) {
        String roleResult = permissionService.checkUserPermission(request, response);
        model.addAttribute(PermissionUtils.JWT_TOKEN_ROLE, roleResult);
        return "/employee/employeelist";
    }

    @RequestMapping(value = "/editPasswordPage")
    public String editPasswordPage(final HttpServletRequest request,
                                   final HttpServletResponse response,
                                   Model model) {
        return "/employee/editPassword";
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

    @RequestMapping(value = "/editpersonalinfo")
    public String editPersonalInfo(final HttpServletRequest request,
                                   final HttpServletResponse response,
                                   Model model) {
        Employee employee = permissionService.getCurrentUser(request, response);
        model.addAttribute("personal_employee", employee);
        String roleResult = permissionService.checkUserPermission(request, response);
        model.addAttribute(PermissionUtils.JWT_TOKEN_ROLE, roleResult);
        return "/employee/editPersonalInfo";
    }

    @RequestMapping(value = "/editPassword")
    @ResponseBody
    public CommonResult<String> editPassword(final HttpServletRequest request,
                                             final HttpServletResponse response,
                                             String oldPassword,
                                             String newPassword,
                                             String rePassword) {
        return employeeService.editPassword(request, response, oldPassword, newPassword, rePassword);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<EmployeeBO>> employeeList(final Integer page,
                                                       final Integer limit,
                                                       String name,
                                                       String idCard) {

        return employeeService.selectList(page, limit, name, idCard);
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
