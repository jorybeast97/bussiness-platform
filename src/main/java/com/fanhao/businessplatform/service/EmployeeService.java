package com.fanhao.businessplatform.service;

import cn.hutool.Hutool;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.common.constant.ResultStatus;
import com.fanhao.businessplatform.enhance.annotation.Operation;
import com.fanhao.businessplatform.entity.BO.EmployeeBO;
import com.fanhao.businessplatform.entity.Department;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.mapper.DepartmentMapper;
import com.fanhao.businessplatform.mapper.EmployeeMapper;
import com.fanhao.businessplatform.utils.CommonUtils;
import com.fanhao.businessplatform.utils.HttpUtils;
import com.fanhao.businessplatform.utils.PermissionUtils;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    public boolean addEmployee(final Employee employee) {
        if (employee == null) return false;
        if (checkUsernameExist(employee.getUsername())) return false;
        employeeMapper.insert(employee);
        return true;
    }

    public CommonResult<String> editPassword(final HttpServletRequest request,
                                             final HttpServletResponse response,
                                             String oldPassword,
                                             String newPassword,
                                             String rePassword) {
        CommonResult<String> commonResult = new CommonResult<>();
        if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(rePassword)){
            commonResult.setMessage("密码不能为空");
            commonResult.setCode(ResultStatus.FAILED.getResultCode());
            return commonResult;
        }
        if (!newPassword.equals(rePassword)){
            commonResult.setMessage("重复输入密码不一致！");
            commonResult.setCode(ResultStatus.FAILED.getResultCode());
            return commonResult;
        }
        Employee employee = getCurrentlyLoggedInUser(request, response);
        String oldPasswordAfterMD5 = SecureUtil.md5(oldPassword);
        if (!oldPasswordAfterMD5.equals(employee.getPassword())) {
            commonResult.setMessage("旧密码不正确");
            commonResult.setCode(ResultStatus.FAILED.getResultCode());
            return commonResult;
        }
        employee.setPassword(SecureUtil.md5(newPassword));
        employeeMapper.updateById(employee);
        commonResult.setMessage("修改成功,请刷新页面重新登录");
        commonResult.setCode(ResultStatus.SUCCESS.getResultCode());
        //注销当前用户
        HttpUtils.writeCookie(response, PermissionUtils.TOKEN, null, 0);
        return commonResult;
    }

    public CommonResult<List<EmployeeBO>> searchByNameAndIdCard(String name,
                                                                String idCard) {
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) wrapper.eq("name", name);
        if (!StringUtils.isEmpty(idCard)) wrapper.eq("id_card", idCard);
        List<Employee> employeeList = employeeMapper.selectList(wrapper);
        List<EmployeeBO> bos = new ArrayList<>();
        employeeList.forEach(employee -> {
            bos.add(generateEmployeeBO(employee));
        });
        CommonResult<List<EmployeeBO>> result = new CommonResult<>();
        result.setCode(ResultStatus.LAYUI_SUCCESS.getResultCode());
        result.setCount(Long.valueOf(bos.size()));
        result.setData(bos);
        return result;
    }

    @Operation(operation = "删除员工信息")
    public CommonResult<String> deleteEmployee(final Integer id) {
        CommonResult<String> commonResult = new CommonResult<>();
        if (id == null) {
            commonResult.setMessage("员工不存在");
            commonResult.setCode(ResultStatus.FAILED.getResultCode());
        }
        else {
            employeeMapper.deleteById(id);
            commonResult.setMessage("删除成功");
            commonResult.setCode(ResultStatus.SUCCESS.getResultCode());
        }
        return commonResult;
    }

    public boolean updateEmployee(final Employee employee) {
        return employeeMapper.updateById(employee) > 0;
    }

    public Employee selectEmployeeById(final Integer id) {
        return employeeMapper.selectById(id);
    }

    public CommonResult<List<EmployeeBO>> selectList(final Integer pageNum,
                                                     final Integer pageSize,
                                                     String name,
                                                     String idCard) {
        IPage<Employee> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) employeeQueryWrapper.eq("name", name);
        if (!StringUtils.isEmpty(idCard)) employeeQueryWrapper.eq("id_card", idCard);
        page = employeeMapper.selectPage(page,employeeQueryWrapper);
        List<Employee> employeeList = page.getRecords();
        List<EmployeeBO> employeeBOS = new ArrayList<>();
        employeeList.forEach(employee -> {
            employeeBOS.add(generateEmployeeBO(employee));
        });
        CommonResult<List<EmployeeBO>> commonResult = new CommonResult<>();
        commonResult.setCount(page.getTotal());
        commonResult.setCode(ResultStatus.LAYUI_SUCCESS.getResultCode());
        commonResult.setData(employeeBOS);
        return commonResult;
    }

    public List<Employee> selectEmployeeByName(final int pageNum,
                                               final int pageSize,
                                               final String name) {
        IPage<Employee> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        page = employeeMapper.selectPage(page, queryWrapper);
        return page.getRecords();
    }

    public Employee selectEmployeeByUsername(final String username) {
        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
        employeeQueryWrapper.eq("username", username);
        return employeeMapper.selectOne(employeeQueryWrapper);
    }

    public boolean checkUsernameExist(final String username) {
        if (StringUtils.isEmpty(username)) return true;
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Employee employee = employeeMapper.selectOne(queryWrapper);
        return employee != null;
    }

    public Employee getCurrentlyLoggedInUser(final HttpServletRequest request,
                                             final HttpServletResponse response) {
        String token = HttpUtils.getCookie(request, PermissionUtils.TOKEN);
        String username = PermissionUtils.getClaimsInformation(token).get(PermissionUtils.JWT_TOKEN_USERNAME);
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        Employee employee = employeeMapper.selectOne(wrapper);
        return employee;
    }

    public EmployeeBO generateEmployeeBO(final Employee employee) {
        if (employee == null) {
            LOGGER.warn("generateEmployeeBO - 参数为空，无法创建对应BO");
            return null;
        }
        Department department = departmentMapper.selectById(employee.getDepartment());
        return new EmployeeBO(employee, department);
    }

    @Operation(operation = "新增或更新用户信息")
    public CommonResult<String> addOrUpdateEmployeeByArgs(Integer id, String username, String password,
                                                          String name, String address, Boolean gender,
                                                          String phone, String email, Integer department,
                                                          String position, String role, String birthday,
                                                          String idCard, String school, String contractStartDate,
                                                          String quitDate, Integer workAge, Boolean status,
                                                          String remark) {
        CommonResult<String> commonResult = new CommonResult<>();
        password = SecureUtil.md5(password);
        Employee employee = generateEmployee(id, username, password, name, address, gender, phone, email, department, position, role, birthday, idCard, school, contractStartDate, quitDate, workAge, status, remark);
        if (id == null) {
            if (checkUsernameExist(username)){
                commonResult.setMessage("用户名已经存在,请重新输入");
                return commonResult;
            }
            employeeMapper.insert(employee);
            commonResult.setMessage("添加成功");
        }else {
            employeeMapper.updateById(employee);
            commonResult.setMessage("更新成功");
        }
        return commonResult;
    }

    /**
     * 封装参数
     */
    private Employee generateEmployee(Integer id, String username, String password,
                                     String name, String address, Boolean gender,
                                     String phone, String email, Integer department,
                                     String position, String role, String birthday,
                                     String idCard, String school, String contractStartDate,
                                     String quitDate, Integer workAge, Boolean status,
                                     String remark) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setUsername(username);
        employee.setPassword(password);
        employee.setName(name);
        employee.setAddress(address);
        employee.setGender(gender);
        employee.setPhone(phone);
        employee.setEmail(email);
        employee.setDepartment(department);
        employee.setPosition(position);
        employee.setRole(role);
        employee.setBirthday(CommonUtils.parseDateFromString(birthday));
        employee.setIdCard(idCard);
        employee.setSchool(school);
        employee.setContractStartDate(CommonUtils.parseDateFromString(contractStartDate));
        employee.setQuitDate(CommonUtils.parseDateFromString(quitDate));
        employee.setWorkAge(workAge);
        employee.setStatus(status);
        employee.setRemark(remark);
        return employee;
    }
}
