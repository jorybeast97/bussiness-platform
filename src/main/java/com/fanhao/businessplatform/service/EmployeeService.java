package com.fanhao.businessplatform.service;

import cn.hutool.Hutool;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.common.constant.ResultStatus;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.mapper.EmployeeMapper;
import com.fanhao.businessplatform.utils.HttpUtils;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class EmployeeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 登录操作
     * @param request
     * @param response
     * @param username
     * @param password
     * @return
     */
    public CommonResult<String> login(final HttpServletRequest request,
                                      final HttpServletResponse response,
                                      final String username,
                                      final String password) {
        boolean isSuccess = employeeInfoCheck(username, password);
        CommonResult<String> commonResult = new CommonResult<>();
        if (!isSuccess) {
            commonResult.setCode(ResultStatus.FAILED.getResultCode());
            commonResult.setMessage(ResultStatus.FAILED.getStatus());
            return commonResult;
        }
        LOGGER.info("用户 : " + username + " 登录成功，IP地址为 : " + request.getRemoteHost());
        commonResult.setCode(ResultStatus.SUCCESS.getResultCode());
        commonResult.setMessage(ResultStatus.SUCCESS.getAttachMessage());
        return commonResult;
    }

    /**
     * 校验用户信息，过程中使用MD5进行加密
     * @param username
     * @param password 明文密码
     * @return
     */
    public boolean employeeInfoCheck(final String username, final String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) return false;
        //MD5加密密码
        String md5Password = SecureUtil.md5(password);
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
                .eq("password", md5Password);
        return employeeMapper.selectCount(wrapper) > 0;
    }

    public boolean addEmployee(final Employee employee) {
        if (employee == null) return false;
        if (checkUsernameExist(employee.getUsername())) return false;
        employeeMapper.insert(employee);
        return true;
    }

    public boolean deleteEmployee(final int id) {
        return employeeMapper.deleteById(id) > 0;
    }

    public boolean updateEmployee(final Employee employee) {
        return employeeMapper.updateById(employee) > 0;
    }

    public Employee selectEmployeeById(final Integer id) {
        return employeeMapper.selectById(id);
    }

    public List<Employee> selectAllEmployee(final Integer pageNum, final Integer pageSize) {
        IPage<Employee> page = new Page<>(pageNum, pageSize);
        page = employeeMapper.selectPage(page,null);
        return page.getRecords();
    }

    public CommonResult<List<Employee>> selectList(final Integer pageNum, final Integer pageSize) {
        IPage<Employee> page = new Page<>(pageNum, pageSize);
        page = employeeMapper.selectPage(page,null);
        CommonResult<List<Employee>> commonResult = new CommonResult<>();
        commonResult.setCount(page.getTotal());
        commonResult.setCode(ResultStatus.LAYUI_SUCCESS.getResultCode());
        commonResult.setData(page.getRecords());
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

    public boolean checkUsernameExist(final String username) {
        if (StringUtils.isEmpty(username)) return true;
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Employee employee = employeeMapper.selectOne(queryWrapper);
        return employee != null;
    }


}
