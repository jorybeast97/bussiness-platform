package com.fanhao.businessplatform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fanhao.businessplatform.entity.Department;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.mapper.DepartmentMapper;
import com.fanhao.businessplatform.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 为首页提供展示信息
 */
@Service("consoleService")
public class ConsoleService {
    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private EmployeeMapper employeeMapper;


    /**
     * 整合员工人数，男女，部门等相关信息
     * @return
     */
    public HashMap<String, List<String>> getEmployeeNumberOverview() {
        HashMap<String, List<String>> map = new HashMap<>();
        List<String> all = new ArrayList<>();
        List<String> man = new ArrayList<>();
        List<String> woman = new ArrayList<>();
        List<String> departmentName = new ArrayList<>();
        //查询所有的部门
        List<Department> departmentList = departmentMapper.selectList(null);
        departmentList.forEach(department -> {
            //获取部门总人数
            QueryWrapper<Employee> allEmployeeWrapper = new QueryWrapper<>();
            allEmployeeWrapper.eq("department", department.getId());
            Integer allNumber = employeeMapper.selectList(allEmployeeWrapper).size();
            all.add(String.valueOf(allNumber));
            //获取部门男性人数
            QueryWrapper<Employee> manEmployeeWrapper = new QueryWrapper<>();
            manEmployeeWrapper.eq("department", department.getId()).eq("gender", '0');
            Integer manNumber = employeeMapper.selectList(manEmployeeWrapper).size();
            man.add(String.valueOf(manNumber));
            //获取部门女性人数
            QueryWrapper<Employee> womanEmployeeWrapper = new QueryWrapper<>();
            womanEmployeeWrapper.eq("department", department.getId()).eq("gender", '1');
            Integer womanNumber = employeeMapper.selectList(womanEmployeeWrapper).size();
            woman.add(String.valueOf(womanNumber));
            //将部门名称添加到部门数组中
            departmentName.add(department.getDepartmentName());
        });
        map.put("all", all);
        map.put("man", man);
        map.put("woman", woman);
        map.put("departmentName", departmentName);
        return map;
    }

}
