package com.fanhao.businessplatform.service;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.common.constant.ResultStatus;
import com.fanhao.businessplatform.entity.BO.MessageBO;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.entity.Message;
import com.fanhao.businessplatform.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service("messageService")
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private EmployeeService employeeService;

    public CommonResult<String> deleteMessage(Integer id) {
        CommonResult<String> commonResult = new CommonResult<>();
        messageMapper.deleteById(id);
        commonResult.setCode(ResultStatus.SUCCESS.getResultCode());
        commonResult.setMessage("删除完成");
        return commonResult;
    }

    public MessageBO viewMessage(Integer id) {
        Message message = messageMapper.selectById(id);
        message.setStatus(true);
        messageMapper.updateById(message);
        return generateMessageBO(message);
    }

    public List<String> getEchartsInformation(final HttpServletRequest request,
                                              final HttpServletResponse response) {
        long oneDayToMills = 86400000;
        Employee employee = employeeService.getCurrentlyLoggedInUser(request, response);
        List<String> result = new ArrayList<>();
        //获取上周时间
        Date preWeekStartTime = DateUtil.beginOfWeek(DateUtil.lastWeek());
        for (int i = 0; i < 7; i++) {
            //获取结束时间
            Date preWeekEndTime = new Date(preWeekStartTime.getTime() + oneDayToMills);
            QueryWrapper<Message> wrapper = new QueryWrapper<>();
            wrapper.eq("sender", employee.getId())
                    .between("create_time", preWeekStartTime, preWeekEndTime);
            List<Message> list = messageMapper.selectList(wrapper);
            result.add(String.valueOf(list.size()));
            preWeekEndTime = preWeekEndTime;
        }
        return result;
    }

    /**
     * 获取导航栏消息数目
     * @param request
     * @param response
     * @return
     */
    public HashMap<String, String> getNavigationBarMark(final HttpServletRequest request,
                                                        final HttpServletResponse response) {
        HashMap<String, String> data = new HashMap<>();
        Employee employee = employeeService.getCurrentlyLoggedInUser(request, response);
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("receiver", employee.getId()).eq("status", 0);
        List<Message> list = messageMapper.selectList(wrapper);
        data.put("num", String.valueOf(list.size()));
        if (list.isEmpty()){
            data.put("present", "all");
        }else {
            data.put("present", "none");
        }
        return data;
    }


    /**
     * 发送或更新消息
     * @param request
     * @param response
     * @param id
     * @param receiver
     * @param title
     * @param content
     * @return
     */
    public CommonResult<String> addOrUpdateMessage(final HttpServletRequest request,
                                            final HttpServletResponse response,
                                            Integer id,
                                            Integer receiver,
                                            String title,
                                            String content) {
        Employee employee = employeeService.getCurrentlyLoggedInUser(request, response);
        CommonResult<String> commonResult = new CommonResult<>();
        if (id != null) {
            Message message = messageMapper.selectById(id);
            message.setReceiver(receiver);
            message.setTitle(title);
            message.setContent(content);
            messageMapper.updateById(message);
            commonResult.setCode(ResultStatus.SUCCESS.getResultCode());
            commonResult.setMessage("修改完成");
            return commonResult;
        }
        Message message = new Message();
        message.setSender(employee.getId());
        message.setReceiver(receiver);
        message.setTitle(title);
        message.setContent(content);
        message.setCreateTime(new Date());
        message.setStatus(false);
        messageMapper.insert(message);
        commonResult.setCode(ResultStatus.SUCCESS.getResultCode());
        commonResult.setMessage("发送完成");
        return commonResult;
    }

    /**
     * 获取个人发送信息
     * @param request
     * @param response
     * @param pageNum
     * @param pageSize
     * @return
     */
    public CommonResult<List<MessageBO>> getPersonalSendMessage(final HttpServletRequest request,
                                                                final HttpServletResponse response,
                                                                Integer pageNum,
                                                                Integer pageSize) {
        Employee employee = employeeService.getCurrentlyLoggedInUser(request, response);
        IPage<Message> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("sender", employee.getId()).orderByDesc("create_time");
        List<Message> list = messageMapper.selectPage(page, wrapper).getRecords();
        List<MessageBO> data = new ArrayList<>();
        list.forEach(message -> {
            data.add(generateMessageBO(message));
        });
        CommonResult<List<MessageBO>> commonResult = new CommonResult<>();
        commonResult.setData(data);
        commonResult.setCount(page.getTotal());
        commonResult.setCode(ResultStatus.LAYUI_SUCCESS.getResultCode());
        return commonResult;
    }

    /**
     * 获取个人接收消息
     * @param request
     * @param response
     * @param pageNum
     * @param pageSize
     * @return
     */
    public CommonResult<List<MessageBO>> getPersonalReceiveMessage(final HttpServletRequest request,
                                                                final HttpServletResponse response,
                                                                Integer pageNum,
                                                                Integer pageSize) {
        Employee employee = employeeService.getCurrentlyLoggedInUser(request, response);
        IPage<Message> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("receiver", employee.getId()).orderByDesc("create_time");
        List<Message> list = messageMapper.selectPage(page, wrapper).getRecords();
        List<MessageBO> data = new ArrayList<>();
        list.forEach(message -> {
            data.add(generateMessageBO(message));
        });
        CommonResult<List<MessageBO>> commonResult = new CommonResult<>();
        commonResult.setData(data);
        commonResult.setCount(page.getTotal());
        commonResult.setCode(ResultStatus.LAYUI_SUCCESS.getResultCode());
        return commonResult;
    }


    public MessageBO generateMessageBO(final Message message) {
        Employee sender = employeeService.selectEmployeeById(message.getSender());
        Employee receiver = employeeService.selectEmployeeById(message.getReceiver());
        return new MessageBO(message, sender, receiver);
    }
}
