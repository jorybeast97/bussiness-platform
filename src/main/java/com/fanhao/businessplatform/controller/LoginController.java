package com.fanhao.businessplatform.controller;

import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.service.EmployeeService;
import com.fanhao.businessplatform.service.LoginService;
import com.fanhao.businessplatform.service.PermissionService;
import com.fanhao.businessplatform.utils.HttpUtils;
import com.fanhao.businessplatform.utils.PermissionUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller("login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private PermissionService permissionService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "/login";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String indexPage(final HttpServletRequest request,
                            final HttpServletResponse response,
                            Model model) {
        String name = PermissionUtils.
                getClaimsInformation(HttpUtils.getCookie(request, PermissionUtils.TOKEN)).get("name");
        String roleResult = permissionService.checkUserPermission(request, response);
        model.addAttribute(PermissionUtils.JWT_TOKEN_NAME, name);
        model.addAttribute(PermissionUtils.JWT_TOKEN_ROLE, roleResult);
        return "index";
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<String> login(final HttpServletRequest httpServletRequest,
                                                final HttpServletResponse httpServletResponse,
                                                final String username,
                                                final String password) {
        return loginService.loginCheck(httpServletRequest, httpServletResponse, username, password);
    }

    @RequestMapping(value = "/logout")
    public void logout(final HttpServletRequest request,
                       final HttpServletResponse response) {
        loginService.Logout(request, response);
    }
}
