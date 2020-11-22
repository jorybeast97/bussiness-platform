package com.fanhao.businessplatform.service;

import cn.hutool.Hutool;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.sun.java.swing.plaf.windows.WindowsTextAreaUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("taskService")
public class TaskService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private TaskMapper taskMapper;

    //任务状态 1:未开始 2:准备中 3:正在进行 4:已完成 5:失败
    public List<Map<String, Object>> getSummaryGraphInformation() {
        List<Map<String, Object>> resultList = new ArrayList<>();
        QueryWrapper<Task> taskQueryWrapper = new QueryWrapper<>();
        Date currMothStartTime = DateUtil.beginOfMonth(new Date());
        Date nextMothStartTime = DateUtil.beginOfMonth(DateUtil.nextMonth());
        taskQueryWrapper.between("create_date", currMothStartTime, nextMothStartTime);
        List<Task> taskList = taskMapper.selectList(taskQueryWrapper);
        Map<String, Object> notStart = initSummaryGraphMap("未开始");
        Map<String, Object> ready = initSummaryGraphMap("准备中");
        Map<String, Object> running = initSummaryGraphMap("进行中");
        Map<String, Object> finished = initSummaryGraphMap("已完成");
        Map<String, Object> failed = initSummaryGraphMap("失败");
        taskList.forEach(task -> {
            switch (task.getTaskStatus()){
                case 1 :
                    notStart.put("value", (Integer) notStart.get("value") + 1); break;
                case 2:
                    ready.put("value", (Integer) ready.get("value") + 1); break;
                case 3:
                    running.put("value", (Integer) running.get("value") + 1); break;
                case 4:
                    finished.put("value", (Integer) finished.get("value") + 1); break;
                case 5:
                    failed.put("value", (Integer) failed.get("value") + 1); break;
                default:
                    break;
            }
        });
        resultList.add(failed);
        resultList.add(notStart);
        resultList.add(ready);
        resultList.add(running);
        resultList.add(finished);
        return resultList;
    }

    public HashMap<String, Object> initSummaryGraphMap(String name) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("value", 0);
        return result;
    }

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
