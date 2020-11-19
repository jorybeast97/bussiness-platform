package com.fanhao.businessplatform.controller;

import cn.hutool.core.util.RandomUtil;
import com.fanhao.businessplatform.service.ConsoleService;
import com.fanhao.businessplatform.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller("console")
@RequestMapping("/console")
public class ConsoleController {

    @Autowired
    private ConsoleService consoleService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String consolePage(Model model) throws UnknownHostException {
        model.addAttribute("cpu_info", CommonUtils.cpuLoad());
        model.addAttribute("memory_info", CommonUtils.memoryLoad());
        model.addAttribute("localhost", InetAddress.getLocalHost());
        model.addAttribute("jdk_version", CommonUtils.jdkInfo());
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) list.add(String.valueOf(i));
        model.addAttribute("afterlist", list);
        return "/overview/console";
    }

    @RequestMapping("/getInfo")
    @ResponseBody
    public HashMap<String,List<String>> getInfo() throws InterruptedException {

        return consoleService.getEmployeeNumberOverview();
    }

}
