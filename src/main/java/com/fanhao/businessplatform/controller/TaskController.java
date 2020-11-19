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

import java.util.List;

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
