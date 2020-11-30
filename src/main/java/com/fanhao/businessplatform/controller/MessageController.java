package com.fanhao.businessplatform.controller;

import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.entity.BO.MessageBO;
import com.fanhao.businessplatform.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller("message")
@RequestMapping(value = "/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/personalMessage")
    public String personalMessagePage() {
        return "/message/personalMessage";
    }

    @RequestMapping(value = "/viewMessage")
    public String viewMessage(Integer id,
                              Model model) {
        MessageBO data = messageService.viewMessage(id);
        model.addAttribute("message", data);
        return "/message/viewMessage";
    }

    @RequestMapping(value = "/addOrUpdate")
    @ResponseBody
    public CommonResult<String> addOrUpdate(final HttpServletRequest request,
                                            final HttpServletResponse response,
                                            Integer id,
                                            Integer receiver,
                                            String title,
                                            String content) {
        return messageService.addOrUpdateMessage(request, response, id, receiver, title, content);
    }

    @RequestMapping(value = "/getEchartsInformation")
    @ResponseBody
    public List<String> getEchartsInformation(final HttpServletRequest request,
                                              final HttpServletResponse response) {
        return messageService.getEchartsInformation(request, response);
    }

    @RequestMapping(value = "/getPersonalReceiveMessage")
    @ResponseBody
    public CommonResult<List<MessageBO>> getPersonalReceiveMessage(final HttpServletRequest request,
                                            final HttpServletResponse response,
                                            Integer page,
                                            Integer limit) {
        return messageService.getPersonalReceiveMessage(request, response, page, limit);
    }

    @RequestMapping(value = "/getPersonalSendMessage")
    @ResponseBody
    public CommonResult<List<MessageBO>> getPersonalSendMessage(final HttpServletRequest request,
                                                                   final HttpServletResponse response,
                                                                   Integer page,
                                                                   Integer limit) {
        return messageService.getPersonalSendMessage(request, response, page, limit);
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public CommonResult<String> deleteMessage(Integer id) {
        return messageService.deleteMessage(id);
    }



}
