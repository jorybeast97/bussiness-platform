package com.fanhao.businessplatform.enhance;

import com.fanhao.businessplatform.enhance.annotation.Operation;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.entity.OperationLog;
import com.fanhao.businessplatform.service.EmployeeService;
import com.fanhao.businessplatform.service.OperationLogService;
import com.fanhao.businessplatform.utils.HttpUtils;
import com.fanhao.businessplatform.utils.PermissionUtils;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@Component
@Aspect
public class OperationCollection {
    private static final Logger LOG = LoggerFactory.getLogger(PermissionFilter.class);

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private OperationLogService operationLogService;

    //获取注解切点
    @Pointcut("@annotation(com.fanhao.businessplatform.enhance.annotation.Operation)")
    private void operationPointcut() {

    }

    @After("operationPointcut() && @annotation(annotation)")
    private void collectOperation(Operation annotation) {
        if (StringUtils.isEmpty(annotation.operation())) return;
        String token = HttpUtils.getCookie(httpServletRequest, PermissionUtils.TOKEN);
        Map<String, String> infoMap = PermissionUtils.getClaimsInformation(token);
        String username = infoMap.get(PermissionUtils.JWT_TOKEN_USERNAME);
        Employee employee = employeeService.selectEmployeeByUsername(username);
        if (employee != null) {
            String host = HttpUtils.getIpAddress(httpServletRequest);
            String address = HttpUtils.getAddressResult(host);
            OperationLog operationLog = new OperationLog();
            operationLog.setName(employee.getName());
            operationLog.setUsername(employee.getUsername());
            operationLog.setHost(host);
            operationLog.setAddress(address);
            operationLog.setDescription(annotation.operation());
            operationLog.setOperationTime(new Date());
            operationLogService.addOperationLog(operationLog);
        }
    }
}
