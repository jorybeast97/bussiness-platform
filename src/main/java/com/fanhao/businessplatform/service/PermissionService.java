package com.fanhao.businessplatform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.mapper.EmployeeMapper;
import com.fanhao.businessplatform.utils.HttpUtils;
import com.fanhao.businessplatform.utils.PermissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service("permissionService")
public class PermissionService {
    private static final String NORMAL_ROLE = "normal";

    @Autowired
    private EmployeeMapper employeeMapper;

    public String checkUserPermission(final HttpServletRequest request,
                                      final HttpServletResponse response) {
        //获取Token
        String token = HttpUtils.getCookie(request, PermissionUtils.TOKEN);
        String role = PermissionUtils.getClaimsInformation(token).get(PermissionUtils.JWT_TOKEN_ROLE);
        if (role.equals(NORMAL_ROLE)) return "all";
        else return "none";
    }

    public Employee getCurrentUser(final HttpServletRequest request,
                                   final HttpServletResponse response) {
        String token = HttpUtils.getCookie(request, PermissionUtils.TOKEN);
        String username = PermissionUtils.getClaimsInformation(token).get(PermissionUtils.JWT_TOKEN_USERNAME);
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        Employee employee = employeeMapper.selectOne(wrapper);
        return employee;
    }
}
