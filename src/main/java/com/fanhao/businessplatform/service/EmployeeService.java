package com.fanhao.businessplatform.service;

import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.mapper.EmployeeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class EmployeeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 新增用户，用于注册
     * @param employee
     * @return
     */
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

    public List<Employee> selectAllEmployee(final int pageNum, final int pageSize) {
        IPage<Employee> page = new Page<>(pageNum, pageSize);
        page = employeeMapper.selectPage(page,null);
        return page.getRecords();
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
