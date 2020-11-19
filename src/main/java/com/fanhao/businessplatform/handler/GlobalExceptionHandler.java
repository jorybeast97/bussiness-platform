package com.fanhao.businessplatform.handler;

import com.fanhao.businessplatform.service.ExceptionLogService;
import com.fanhao.businessplatform.utils.HttpUtils;
import com.fanhao.businessplatform.utils.PermissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ExceptionLogService exceptionLogService;

    @ExceptionHandler(value = Exception.class)
    public void HandlerAllException(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Exception e) {
        //优先打印堆栈信息方便异常查阅
        e.printStackTrace();
        String token = HttpUtils.getCookie(request, PermissionUtils.TOKEN);
        exceptionLogService.insertExceptionLog(e, request, response, token);
    }


}
