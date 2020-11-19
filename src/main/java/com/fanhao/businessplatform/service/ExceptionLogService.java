package com.fanhao.businessplatform.service;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fanhao.businessplatform.mapper.ExceptionLogMapper;
import com.sun.jndi.cosnaming.ExceptionMapper;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service("exceptionLogService")
public class ExceptionLogService {

    @Autowired
    private ExceptionLogMapper exceptionLogMapper;

    /**
     * 插入一条Exception数据
     * @param exception
     * @param request
     * @param response
     * @param token
     */
    public void insertExceptionLog(final Exception exception,
                                   final HttpServletRequest request,
                                   final HttpServletResponse response,
                                   String token) {

    }
}
