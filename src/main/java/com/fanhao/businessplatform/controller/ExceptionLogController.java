package com.fanhao.businessplatform.controller;

import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.entity.ExceptionLog;
import com.fanhao.businessplatform.service.ExceptionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller("exceptionLog")
@RequestMapping(value = "/exceptionlog")
public class ExceptionLogController {
    @Autowired
    private ExceptionLogService exceptionLogService;

    @RequestMapping("")
    public String getExceptionLogPage() {
        return "/log/exceptionLogList";
    }

    @RequestMapping("/present")
    public String presentExceptionLogPage(Integer id,
                                          Model model) {
        model.addAttribute("exceptionLog", exceptionLogService.selectExceptionLogById(id));
        return "/log/presentExceptionLog";
    }

    @RequestMapping("/list")
    @ResponseBody
    public CommonResult<List<ExceptionLog>> list(Integer page,
                                                 Integer limit) {
        return exceptionLogService.getExceptionList(page, limit);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public CommonResult<String> delete(Integer id) {
        return exceptionLogService.deleteById(id);
    }
}
