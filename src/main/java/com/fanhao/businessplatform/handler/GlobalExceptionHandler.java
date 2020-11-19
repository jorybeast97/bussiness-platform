package com.fanhao.businessplatform.handler;

import com.fanhao.businessplatform.utils.HttpUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public void getExceptionLog(final HttpServletRequest request,
                                final HttpServletResponse response,
                                Exception e) {
        String ip = HttpUtils.getIpAddress(request);
    }
}
