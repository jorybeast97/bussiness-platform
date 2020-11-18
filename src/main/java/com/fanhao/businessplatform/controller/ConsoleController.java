package com.fanhao.businessplatform.controller;

import com.fanhao.businessplatform.utils.CommonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Controller("console")
@RequestMapping("/console")
public class ConsoleController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String consolePage(Model model) throws UnknownHostException {
        model.addAttribute("cpu_info", CommonUtils.cpuLoad());
        model.addAttribute("memory_info", CommonUtils.memoryLoad());
        model.addAttribute("localhost", "10.130.141.157");
        model.addAttribute("jdk_version", CommonUtils.jdkInfo());
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) list.add(String.valueOf(i));
        model.addAttribute("afterlist", list);
        return "/overview/console";
    }

    @RequestMapping("/getInfo")
    @ResponseBody
    public String getInfo() {
        return "测试数据";
    }
}
