package com.fanhao.businessplatform.controller;

import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.common.constant.ResultStatus;
import com.fanhao.businessplatform.enhance.annotation.PermissionVerification;
import com.fanhao.businessplatform.service.EmployeeService;
import com.fanhao.businessplatform.utils.HttpUtils;
import com.fanhao.businessplatform.utils.PermissionUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/login")
    @ResponseBody
    public CommonResult<String> login(final HttpServletRequest request,
                                      final HttpServletResponse response,
                                      final String username,
                                      final String password) {
        //TODO 数据库校验步骤
        //doSomething
        String token = PermissionUtils.generateJWT(username, "admin", "范昊", null, null);
        HttpUtils.writeCookie(response, HttpUtils.TOKEN, token, null);
        CommonResult<String> commonResult = new CommonResult<>();
        commonResult.setAttachMessage("登录成功");
        commonResult.setData(token);
        commonResult.setResultCode(ResultStatus.SUCCESS.getResultCode());
        return commonResult;
    }

    @RequestMapping(value = "/permissiontest")
    @ResponseBody
    public CommonResult<Claims> testPermission(final HttpServletRequest request,
                                               final HttpServletResponse response) {
        String token = HttpUtils.getCookie(request, HttpUtils.TOKEN);
        CommonResult<Claims> claimsCommonResult =
                PermissionUtils.checkPermissionInfo("admin", "fanhao", token);
        return claimsCommonResult;
    }
}
