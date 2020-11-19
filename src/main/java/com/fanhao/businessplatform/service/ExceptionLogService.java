package com.fanhao.businessplatform.service;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.common.constant.ResultStatus;
import com.fanhao.businessplatform.entity.ExceptionLog;
import com.fanhao.businessplatform.mapper.ExceptionLogMapper;
import com.fanhao.businessplatform.utils.HttpUtils;
import com.fanhao.businessplatform.utils.PermissionUtils;
import com.sun.jndi.cosnaming.ExceptionMapper;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("exceptionLogService")
public class ExceptionLogService {

    @Autowired
    private ExceptionLogMapper exceptionLogMapper;

    public CommonResult<String> deleteById(Integer id) {
        CommonResult<String> commonResult = new CommonResult<>();
        exceptionLogMapper.deleteById(id);
        commonResult.setMessage("删除成功");
        return commonResult;
    }

    public ExceptionLog selectExceptionLogById(Integer id) {
        return exceptionLogMapper.selectById(id);
    }

    /**
     * 获取异常日志信息
     * @param page
     * @param limit
     * @return
     */
    public CommonResult<List<ExceptionLog>> getExceptionList(final Integer page,
                                                             final Integer limit) {
        IPage<ExceptionLog> iPage = new Page<>(page, limit);
        iPage = exceptionLogMapper.selectPage(iPage, null);
        List<ExceptionLog> list = iPage.getRecords();
        CommonResult<List<ExceptionLog>> commonResult = new CommonResult<>();
        commonResult.setData(list);
        commonResult.setCode(ResultStatus.LAYUI_SUCCESS.getResultCode());
        commonResult.setCount(iPage.getTotal());
        return commonResult;
    }

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
        Map<String, String> tokenMap = PermissionUtils.getClaimsInformation(token);
        String username = tokenMap.get(PermissionUtils.JWT_TOKEN_USERNAME);
        String ipAddr = HttpUtils.getIpAddress(request);
        Map<String, String> addressMap = HttpUtils.getIpAddressInformation(ipAddr);
        String natureAddr = "局域网或本机";
        if (addressMap != null) {
            natureAddr = addressMap.get("province") + addressMap.get("city");
        }
        String exceptionInfo = getExceptionStackInformation(exception);
        Date happenDate = new Date();
        String description = exception.getMessage();
        ExceptionLog exceptionLog = new ExceptionLog();
        exceptionLog.setUsername(username);
        exceptionLog.setHost(ipAddr);
        exceptionLog.setAddress(natureAddr);
        exceptionLog.setDescription(description);
        exceptionLog.setHappenTime(happenDate);
        exceptionLog.setDetail(exceptionInfo);
        exceptionLogMapper.insert(exceptionLog);
    }

    public String getExceptionStackInformation(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
