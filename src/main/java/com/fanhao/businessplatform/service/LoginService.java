package com.fanhao.businessplatform.service;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.common.constant.ResultStatus;
import com.fanhao.businessplatform.enhance.annotation.Operation;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.mapper.EmployeeMapper;
import com.fanhao.businessplatform.utils.HttpUtils;
import com.fanhao.businessplatform.utils.PermissionUtils;
import com.fanhao.businessplatform.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service("loginService")
public class LoginService {
    //默认Cookie有效时间为三天
    private static final int COOKIE_EXPIRE_TIME = 60 * 60 * 24 * 3;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 登录校验
     *
     * @param username
     * @param password 明文密码
     * @return
     */
    public CommonResult<String> loginCheck(final HttpServletRequest request,
                                           final HttpServletResponse response,
                                           final String username,
                                           final String password) {
        CommonResult<String> commonResult = new CommonResult<>();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            commonResult.setCode(ResultStatus.FAILED.getResultCode());
            commonResult.setMessage("用户名或密码不能为空");
            return commonResult;
        }
        //MD5加密密码
        String md5Password = SecureUtil.md5(password);
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
                .eq("password", md5Password);
        Employee employee = employeeMapper.selectOne(wrapper);
        boolean success = employee == null ? false : true;
        if (!success) {
            commonResult.setCode(ResultStatus.FAILED.getResultCode());
            commonResult.setMessage("用户名或密码错误");
            return commonResult;
        }
        //登陆成功后，将TOKEN写入Cookie
        final String jwtToken = generateEmployeeToken(request, response, employee);
        //写入Cookie
        HttpUtils.writeCookie(response, PermissionUtils.TOKEN, jwtToken, LoginService.COOKIE_EXPIRE_TIME);
        commonResult.setCode(ResultStatus.SUCCESS.getResultCode());
        //异步提交，避免出现异常影响其他业务逻辑工作
        ThreadUtils.getThreadPoolHelper().execute(() -> {
            operationLogService.addLogWhenLogin(employee, request, response);
        });
        commonResult.setMessage("登录成功");
        return commonResult;
    }

    @Operation(operation = "退出登录")
    public void Logout(final HttpServletRequest request,
                       final HttpServletResponse response) {
        HttpUtils.writeCookie(response, PermissionUtils.JWT_TOKEN_USERNAME, "", 0);
    }

    public String generateEmployeeToken(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final Employee employee) {
        String username = employee.getUsername();
        String name = employee.getName();
        String role = employee.getRole();
        String token = PermissionUtils.generateJWT(username, role, name, PermissionUtils.DEFAULT_TOKEN_EXPIRE_TIME, null);
        return token;
    }
}
