package com.fanhao.businessplatform.controller;

import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.entity.OperationLog;
import com.fanhao.businessplatform.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller("operationLog")
@RequestMapping(value = "/operationlog")
public class OperationLogController {
    @Autowired
    private OperationLogService operationLogService;

    @RequestMapping(value = "")
    public String operationLogPage() {
        return "/log/operationLogList";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<OperationLog>> getOperationList(Integer page,
                                                             Integer limit) {
        return operationLogService.selectAllOperationLog(page, limit);
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public CommonResult<String> delete(Integer id) {
        return operationLogService.deleteById(id);
    }
}
