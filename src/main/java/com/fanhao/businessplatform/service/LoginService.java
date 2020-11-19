package com.fanhao.businessplatform.service;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.common.constant.ResultStatus;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.mapper.EmployeeMapper;
import com.fanhao.businessplatform.utils.HttpUtils;
import com.fanhao.businessplatform.utils.PermissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service("loginService")
public class LoginService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 登录
     * @param username
     * @param password 明文密码
     * @return
     */
    public CommonResult<String> loginCheck(final HttpServletRequest request,
                                           final HttpServletResponse response,
                                           final String username,
                                           final String password) {
        CommonResult<String> commonResult = new CommonResult<>();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            commonResult.setCode(ResultStatus.FAILED.getResultCode());
            commonResult.setMessage("用户名或密码不能为空");
            return commonResult;
        }
        //MD5加密密码
        String md5Password = SecureUtil.md5(password);
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
                .eq("password", md5Password);
        boolean success = employeeMapper.selectCount(wrapper) > 0;
        if (!success){
            commonResult.setCode(ResultStatus.FAILED.getResultCode());
            commonResult.setMessage("用户名或密码错误");
            return commonResult;
        }
        //登陆成功后，将TOKEN写入Cookie
        HttpUtils.writeCookie(response, PermissionUtils.JWT_TOKEN_USERNAME, username, 3600 * 24);
        commonResult.setCode(ResultStatus.SUCCESS.getResultCode());
        commonResult.setMessage("登录成功");
        return commonResult;
    }

    public void Logout(final HttpServletRequest request,
                       final HttpServletResponse response) {
        HttpUtils.writeCookie(response, PermissionUtils.JWT_TOKEN_USERNAME, "", 0);
    }
}
