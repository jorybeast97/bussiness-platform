package com.fanhao.businessplatform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.common.constant.ResultStatus;
import com.fanhao.businessplatform.entity.BO.EmployeeBO;
import com.fanhao.businessplatform.entity.BO.TaskBO;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.entity.Task;
import com.fanhao.businessplatform.mapper.EmployeeMapper;
import com.fanhao.businessplatform.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("taskService")
public class TaskService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private TaskMapper taskMapper;

    public TaskBO selectTaskBOById(Integer id) {
        Task task = taskMapper.selectById(id);
        return generateTaskBO(task);
    }

    public CommonResult<List<TaskBO>> selectAll(final Integer pageNum,
                                                final Integer pageSize) {
        IPage<Task> page = new Page<>(pageNum, pageSize);
        page = taskMapper.selectPage(page, null);
        List<Task> taskList = page.getRecords();
        List<TaskBO> taskBOList = new ArrayList<>();
        taskList.forEach(task -> {
            taskBOList.add(generateTaskBO(task));
        });
        CommonResult<List<TaskBO>> commonResult = new CommonResult<>();
        commonResult.setCode(ResultStatus.LAYUI_SUCCESS.getResultCode());
        commonResult.setData(taskBOList);
        commonResult.setCount(page.getTotal());
        return commonResult;
    }

    public CommonResult<String> addOrUpdateTask(Integer id,
                                                String taskName,
                                                String taskContent,
                                                Integer sender,
                                                Integer receiver,
                                                Integer taskStatus,
                                                String remark,
                                                String createDate,
                                                String endDate) {
        Task task = generateTask(id, taskName, taskContent, sender, receiver, taskStatus, remark, createDate, endDate);
        CommonResult<String> commonResult = new CommonResult<>();
        if (task.getId() == null) {
            taskMapper.insert(task);
            commonResult.setMessage("添加完成");
        }else {
            taskMapper.updateById(task);
            commonResult.setMessage("修改完成");
        }
        return commonResult;
    }

    public CommonResult<String> deleteTaskById(Integer id) {
        CommonResult<String> commonResult = new CommonResult<>();
        taskMapper.deleteById(id);
        commonResult.setMessage("删除完成");
        return commonResult;
    }

    /**
     * 生成对应的TaskBO
     * @param task
     * @return
     */
    public TaskBO generateTaskBO(final Task task) {
        Employee sender = employeeMapper.selectById(task.getSender());
        Employee receiver = employeeMapper.selectById(task.getReceiver());
        return new TaskBO(task, sender, receiver);
    }

    public Task generateTask(Integer id,
                             String taskName,
                             String taskContent,
                             Integer sender,
                             Integer receiver,
                             Integer taskStatus,
                             String remark,
                             String createDate,
                             String endDate) {
        Task task = new Task(id, taskName, taskContent, sender, receiver, taskStatus, remark, createDate, endDate);
        return task;
    }
}
