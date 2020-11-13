package com.fanhao.businessplatform.controller;

import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.common.constant.ResultStatus;
import com.fanhao.businessplatform.enhance.annotation.PermissionVerification;
import com.fanhao.businessplatform.service.EmployeeService;
import com.fanhao.businessplatform.utils.HttpUtils;
import com.fanhao.businessplatform.utils.PermissionUtils;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private Gson gson;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public String login(final HttpServletRequest request,
                                      final HttpServletResponse response,
                                      final String username,
                                      final String password) {
        CommonResult<String> result = employeeService.login(request, response, username, password);

        return gson.toJson(result);
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

    @RequestMapping(value = "/loginAction", method = RequestMethod.GET)
    public String directLoginPage(final HttpServletRequest httpServletRequest,
                                  final HttpServletResponse httpServletResponse,
                                  final String username,
                                  final String password) {
        if (check(username, password)){
            return "index";
        } else {
            return "login";
        }
    }

    @RequestMapping(value = "/freemark", method = RequestMethod.GET)
    public String test() {
        return "freemark";
    }
    public boolean check(String username, String password) {

        if (username != null && password != null) return true;
        else return false;
    }
}
