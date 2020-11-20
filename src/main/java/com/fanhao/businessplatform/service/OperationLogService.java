package com.fanhao.businessplatform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.common.constant.ResultStatus;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.entity.OperationLog;
import com.fanhao.businessplatform.mapper.OperationLogMapper;
import com.fanhao.businessplatform.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("operationLogService")
public class OperationLogService {
    @Autowired
    private OperationLogMapper operationLogMapper;

    public CommonResult<String> deleteById(Integer id) {
        CommonResult<String> commonResult = new CommonResult<>();
        operationLogMapper.deleteById(id);
        commonResult.setMessage("删除完成");
        commonResult.setCode(ResultStatus.SUCCESS.getResultCode());
        return commonResult;
    }

    public void addOperationLog(final OperationLog operationLog) {
        operationLogMapper.insert(operationLog);
    }

    public CommonResult<List<OperationLog>> selectAllOperationLog(Integer pageNum,
                                                                  Integer pageSize) {
        IPage<OperationLog> page = new Page<>(pageNum, pageSize);
        page = operationLogMapper.selectPage(page, null);
        List<OperationLog> list = page.getRecords();
        CommonResult<List<OperationLog>> commonResult = new CommonResult<>();
        commonResult.setData(list);
        commonResult.setCode(ResultStatus.LAYUI_SUCCESS.getResultCode());
        commonResult.setCount(page.getTotal());
        return commonResult;
    }

    public void addLogWhenLogin(final Employee employee,
                                final HttpServletRequest request,
                                final HttpServletResponse response) {
        String host = HttpUtils.getIpAddress(request);
        Map<String, String> map = HttpUtils.getIpAddressInformation(host);
        String address = "局域网或本机";
        if (map != null) {
            address = map.get("province") + map.get("city");
        }
        String description = "登录人事管理系统";
        OperationLog operationLog = new OperationLog();
        operationLog.setAddress(address);
        operationLog.setDescription(description);
        operationLog.setHost(host);
        operationLog.setName(employee.getName());
        operationLog.setUsername(employee.getUsername());
        operationLog.setOperationTime(new Date());
        operationLogMapper.insert(operationLog);
    }
}
