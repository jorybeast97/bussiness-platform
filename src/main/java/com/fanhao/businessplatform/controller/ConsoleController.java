package com.fanhao.businessplatform.controller;

import cn.hutool.core.util.RandomUtil;
import com.fanhao.businessplatform.service.ConsoleService;
import com.fanhao.businessplatform.utils.CommonUtils;
import com.fanhao.businessplatform.utils.HttpUtils;
import com.fanhao.businessplatform.utils.PermissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("console")
@RequestMapping("/console")
public class ConsoleController {

    @Autowired
    private ConsoleService consoleService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String consolePage(final HttpServletRequest request,
                              final HttpServletResponse response,
                              Model model) throws UnknownHostException {
        model.addAttribute("cpu_info", CommonUtils.cpuLoad());
        model.addAttribute("memory_info", CommonUtils.memoryLoad());
        model.addAttribute("ip_address", "S");
        model.addAttribute("jdk_version", CommonUtils.jdkInfo());

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) list.add(String.valueOf(i));
        model.addAttribute("afterlist", list);
        return "/overview/console";
    }

    @RequestMapping("/getPersonNumInfo")
    @ResponseBody
    public HashMap<String, List<String>> getInfo() throws InterruptedException {
        return consoleService.getEmployeeNumberOverview();
    }

    @RequestMapping("/getSummaryGraphInfo")
    @ResponseBody
    public List<Map<String, Object>> getSummaryGraphInformation() {
        return consoleService.getSummaryGraphInformation();
    }

    @RequestMapping(value = "/getSalaryInformation")
    @ResponseBody
    public Map<String, Object> getSalaryInformation() {
        return consoleService.getSalaryInformation();
    }
}
