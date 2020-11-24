package com.fanhao.businessplatform.service;

import cn.hutool.Hutool;
import cn.hutool.core.date.DateTime;
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
import com.fanhao.businessplatform.utils.CommonUtils;
import com.fanhao.businessplatform.utils.HttpUtils;
import com.fanhao.businessplatform.utils.PermissionUtils;
import com.sun.java.swing.plaf.windows.WindowsTextAreaUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service("taskService")
public class TaskService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ConsoleService consoleService;

    public CommonResult<List<TaskBO>> getPersonalTaskInfo(final HttpServletRequest request,
                                                          final HttpServletResponse response,
                                                          final Integer pageNum,
                                                          final Integer pageSize) {
        //获取当前用户
        Employee employee = employeeService.getCurrentlyLoggedInUser(request, response);
        IPage<Task> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Task> wrapper = new QueryWrapper<>();
        wrapper.eq("sender", employee.getId())
                .or()
                .eq("receiver", employee.getId());
        List<Task> taskList = taskMapper.selectPage(page, wrapper).getRecords();
        List<TaskBO> data = new ArrayList<>();
        taskList.forEach(task -> {
            data.add(generateTaskBO(task));
        });
        CommonResult<List<TaskBO>> commonResult = new CommonResult<>();
        commonResult.setData(data);
        commonResult.setCode(ResultStatus.LAYUI_SUCCESS.getResultCode());
        commonResult.setCount(page.getTotal());
        return commonResult;
    }

    public List<TaskBO> getLeastTaskInformation(final HttpServletRequest request,
                                                final HttpServletResponse response) {
        String token = HttpUtils.getCookie(request, PermissionUtils.TOKEN);
        String username = PermissionUtils.getClaimsInformation(token).get(PermissionUtils.JWT_TOKEN_USERNAME);
        //查询对应的员工
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        Employee employee = employeeMapper.selectOne(wrapper);
        //查询对应个人任务
        QueryWrapper<Task> taskQueryWrapper = new QueryWrapper<>();
        taskQueryWrapper.eq("receiver", employee.getId())
                .orderByAsc("end_date");
        List<Task> taskList = taskMapper.selectList(taskQueryWrapper);
        List<TaskBO> result = new ArrayList<>();
        taskList.forEach(task -> {
            result.add(generateTaskBO(task));
        });
        return result;
    }

    public Map<String, Object> getPersonalTaskLineChartInformation(final HttpServletRequest request,
                                                                   final HttpServletResponse response) {
        Employee employee = employeeService.getCurrentlyLoggedInUser(request, response);
        List<Integer> runningTaskNum = new ArrayList<>();
        List<Integer> finishedTaskNum = new ArrayList<>();
        List<Integer> allTaskNum = new ArrayList<>();
        List<DateTime> dateList = consoleService.getCurrentMonthDateList();
        //个人进行中
        for (int i = 0; i < dateList.size(); i++) {
            QueryWrapper<Task> wrapper = new QueryWrapper<>();
            if (i < dateList.size() - 1) {
                wrapper.between("create_date", dateList.get(i), dateList.get(i + 1)).
                        eq("receiver", employee.getId()).eq("task_status",3);
                List<Task> list = taskMapper.selectList(wrapper);
                runningTaskNum.add(list.size());
            }else {
                wrapper.between("create_date", dateList.get(i), DateUtil.endOfMonth(new Date())).
                        eq("receiver", employee.getId()).eq("task_status",4);
                List<Task> list = taskMapper.selectList(wrapper);
                runningTaskNum.add(list.size());
            }
        }
        //个人完成数量
        for (int i = 0; i < dateList.size(); i++) {
            QueryWrapper<Task> wrapper = new QueryWrapper<>();
            if (i < dateList.size() - 1) {
                wrapper.between("create_date", dateList.get(i), dateList.get(i + 1)).
                        eq("receiver", employee.getId()).eq("task_status",4);
                List<Task> list = taskMapper.selectList(wrapper);
                finishedTaskNum.add(list.size());
            }else {
                wrapper.between("create_date", dateList.get(i), DateUtil.endOfMonth(new Date())).
                        eq("receiver", employee.getId()).eq("task_status",4);
                List<Task> list = taskMapper.selectList(wrapper);
                finishedTaskNum.add(list.size());
            }
        }
        //个人总数量
        for (int i = 0; i < dateList.size(); i++) {
            QueryWrapper<Task> wrapper = new QueryWrapper<>();
            if (i < dateList.size() - 1) {
                wrapper.between("create_date", dateList.get(i), dateList.get(i + 1)).
                        eq("receiver", employee.getId());
                List<Task> list = taskMapper.selectList(wrapper);
                allTaskNum.add(list.size());
            }else {
                wrapper.between("create_date", dateList.get(i), DateUtil.endOfMonth(new Date())).
                        eq("receiver", employee.getId());
                List<Task> list = taskMapper.selectList(wrapper);
                allTaskNum.add(list.size());
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("runningTaskNum", runningTaskNum);
        result.put("finishedTaskNum", finishedTaskNum);
        result.put("allTaskNum", allTaskNum);
        return result;
    }

    /**
     * 获取个人薪资页面饼图数据
     * @param request
     * @param response
     * @return
     */
    public Map<String, Object> getPersonalTaskDistribution(final HttpServletRequest request,
                                                   final HttpServletResponse response) {
        Employee employee = employeeService.getCurrentlyLoggedInUser(request, response);
        Date startTime = DateUtil.beginOfMonth(new Date());
        QueryWrapper<Task> wrapper = new QueryWrapper<>();
        wrapper.between("create_date", startTime, new Date())
                .eq("receiver", employee.getId());
        List<Task> taskList = taskMapper.selectList(wrapper);
        Map<String, Object> notStart = initSummaryGraphMap("未开始");
        Map<String, Object> ready = initSummaryGraphMap("准备中");
        Map<String, Object> running = initSummaryGraphMap("进行中");
        Map<String, Object> finished = initSummaryGraphMap("已完成");
        Map<String, Object> failed = initSummaryGraphMap("失败");
        List<Map<String, Object>> resultList = new ArrayList<>();
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
        Map<String, Object> result = new HashMap<>();
        result.put("list", resultList);
        result.put("date", CommonUtils.parseStringFromDate(new Date(), "yyyy年MM月dd日"));
        return result;
    }

    /**
     * 任务状态 1:未开始 2:准备中 3:正在进行 4:已完成 5:失败
     * 获取系统首页任务统计饼图数据
     * @return
     */
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
