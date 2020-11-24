package com.fanhao.businessplatform.controller;

import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.entity.BO.TaskBO;
import com.fanhao.businessplatform.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller("task")
@RequestMapping(value = "/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "")
    public String taskListPage() {
        return "/task/taskList";
    }

    @RequestMapping(value = "/add")
    public String addTaskPage() {
        return "/task/addTask";
    }

    @RequestMapping(value = "/edit")
    public String editTaskPage(Integer id,
                               Model model) {
        TaskBO taskBO = taskService.selectTaskBOById(id);
        model.addAttribute("taskInfo", taskBO);
        return "/task/editTask";
    }

    @RequestMapping(value = "/taskStatistics")
    public String taskStatisticsPage(final HttpServletRequest request,
                                     final HttpServletResponse response,
                                     Model model) {
        List<TaskBO> list = taskService.getLeastTaskInformation(request, response);
        model.addAttribute("least_task", list);
        return "/task/taskStatistics";
    }

    @RequestMapping(value = "/personaltask")
    public String personalTaskPage(final HttpServletRequest request,
                                   final HttpServletResponse response,
                                   Model model) {
        return "/task/personalTask";
    }

    @RequestMapping(value = "/personalTaskInfo")
    @ResponseBody
    public CommonResult<List<TaskBO>> getPersonalTaskInfo(final HttpServletRequest request,
                                                          final HttpServletResponse response,
                                                          final Integer page,
                                                          final Integer limit) {
        return taskService.getPersonalTaskInfo(request, response, page, limit);
    }

    @RequestMapping(value = "/personalTaskLineChart")
    @ResponseBody
    public Map<String, Object> getPersonalTaskLineChartInformation(final HttpServletRequest request,
                                                                   final HttpServletResponse response) {
        return taskService.getPersonalTaskLineChartInformation(request, response);
    }

    @RequestMapping(value = "/personalTaskDistribution")
    @ResponseBody
    public Map<String, Object> getPersonalTaskDistribution(final HttpServletRequest request,
                                                         final HttpServletResponse response) {
        return taskService.getPersonalTaskDistribution(request, response);
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<String> addOrUpdateTask(Integer id,
                                                String taskName,
                                                String taskContent,
                                                Integer sender,
                                                Integer receiver,
                                                Integer taskStatus,
                                                String remark,
                                                String createDate,
                                                String endDate) {
        return taskService.addOrUpdateTask(id, taskName, taskContent, sender, receiver, taskStatus, remark, createDate, endDate);
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public CommonResult<String> delete(Integer id) {
        return taskService.deleteTaskById(id);
    }

    /**
     * 查询所有
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<TaskBO>> getTaskList(Integer page,
                                                  Integer limit) {
        return taskService.selectAll(page, limit);
    }
}
