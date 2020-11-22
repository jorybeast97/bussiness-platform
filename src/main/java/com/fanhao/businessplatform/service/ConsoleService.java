package com.fanhao.businessplatform.service;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fanhao.businessplatform.entity.Department;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.entity.Salary;
import com.fanhao.businessplatform.mapper.DepartmentMapper;
import com.fanhao.businessplatform.mapper.EmployeeMapper;
import com.fanhao.businessplatform.mapper.SalaryMapper;
import com.fanhao.businessplatform.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 为首页提供展示信息
 */
@Service("consoleService")
public class ConsoleService {
    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private SalaryMapper salaryMapper;

    public String getIpAddress(final HttpServletRequest request) {
        Map<String, String> map = HttpUtils.getIpAddressInformation(HttpUtils.getIpAddress(request));
        if (map == null){
            return "未检测到本机地址";
        }
        return map.get("province") + map.get("city");
    }

    public List<Map<String, Object>> getSummaryGraphInformation() {
        List<Map<String, Object>> summaryGraphInformation = taskService.getSummaryGraphInformation();
        return summaryGraphInformation;
    }

    /**
     * 整合员工人数，男女，部门等相关信息
     * @return
     */
    public HashMap<String, List<String>> getEmployeeNumberOverview() {
        HashMap<String, List<String>> map = new HashMap<>();
        List<String> all = new ArrayList<>();
        List<String> man = new ArrayList<>();
        List<String> woman = new ArrayList<>();
        List<String> departmentName = new ArrayList<>();
        //查询所有的部门
        List<Department> departmentList = departmentMapper.selectList(null);
        departmentList.forEach(department -> {
            //获取部门总人数
            QueryWrapper<Employee> allEmployeeWrapper = new QueryWrapper<>();
            allEmployeeWrapper.eq("department", department.getId());
            Integer allNumber = employeeMapper.selectList(allEmployeeWrapper).size();
            all.add(String.valueOf(allNumber));
            //获取部门男性人数
            QueryWrapper<Employee> manEmployeeWrapper = new QueryWrapper<>();
            manEmployeeWrapper.eq("department", department.getId()).eq("gender", '0');
            Integer manNumber = employeeMapper.selectList(manEmployeeWrapper).size();
            man.add(String.valueOf(manNumber));
            //获取部门女性人数
            QueryWrapper<Employee> womanEmployeeWrapper = new QueryWrapper<>();
            womanEmployeeWrapper.eq("department", department.getId()).eq("gender", '1');
            Integer womanNumber = employeeMapper.selectList(womanEmployeeWrapper).size();
            woman.add(String.valueOf(womanNumber));
            //将部门名称添加到部门数组中
            departmentName.add(department.getDepartmentName());
        });
        map.put("all", all);
        map.put("man", man);
        map.put("woman", woman);
        map.put("departmentName", departmentName);
        return map;
    }

    /**
     * 首页展示中薪资部分信息
     * @return
     */
    public Map<String, Object> getSalaryInformation() {
        Map<String, Object> resultMap = new HashMap<>();
        List<String> title = getTitle();
        List<String> preIndex = getSalaryIndexList(getPreMonthDateList());
        List<String> currIndex = getSalaryIndexList(getCurrentMonthDateList());
        List<Double> preSalary = getEveryMonthSalaryInfo(getPreMonthDateList());
        List<Double> currSalary = getEveryMonthSalaryInfo(getCurrentMonthDateList());
        resultMap.put("title", title);
        resultMap.put("currIndex", currIndex);
        resultMap.put("preIndex", preIndex);
        resultMap.put("preSalary", preSalary);
        resultMap.put("currSalary", currSalary);
        return resultMap;
    }


    //获取底部坐标
    public List<String> getSalaryIndexList(List<DateTime> list) {
        List<String> index = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        list.forEach(dateTime -> {
            String timeStr = simpleDateFormat.format((Date) dateTime);
            index.add(timeStr);
        });
        return index;
    }

    public List<String> getTitle() {
        List<String> result = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        Date lastYear = new Date(DateUtil.beginOfYear(new Date()).getTime() - 1000);
        result.add(simpleDateFormat.format(lastYear));
        result.add(simpleDateFormat.format(new Date()));
        return result;
    }

    //获取本年1-12月时间列表
    public List<DateTime> getCurrentMonthDateList() {
        Date begin = DateUtil.beginOfYear(new Date());
        Date end = DateUtil.endOfYear(new Date());
        return DateUtil.rangeToList(begin, end, DateField.MONTH);
    }

    //获取去年1-12月时间列表
    public List<DateTime> getPreMonthDateList() {
        Date currentBegin = DateUtil.beginOfYear(new Date());
        long preYearTime = currentBegin.getTime() - 1000;
        Date begin = DateUtil.beginOfYear(new Date(preYearTime));
        Date end = DateUtil.endOfYear(new Date(preYearTime));
        return DateUtil.rangeToList(begin, end, DateField.MONTH);
    }

    //获取薪资信息
    public List<Double> getEveryMonthSalaryInfo(List<DateTime> timeList) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < timeList.size(); i++) {
            QueryWrapper<Salary> wrapper = new QueryWrapper<>();
            //非最后一位元素
            if (i != timeList.size() - 1) {
                wrapper.between("grant_date", (Date) timeList.get(i), (Date) timeList.get(i + 1));
                List<Salary> salaries = salaryMapper.selectList(wrapper);
                double currentSalarySum = salaries.stream().mapToDouble(Salary::getTotalSalary).sum();
                result.add(currentSalarySum);
            }else {
                Date lastDate = DateUtil.endOfYear(timeList.get(i));
                wrapper.between("grant_date", (Date) timeList.get(i), lastDate);
                List<Salary> salaries = salaryMapper.selectList(wrapper);
                double currentSalarySum = salaries.stream().mapToDouble(Salary::getTotalSalary).sum();
                result.add(currentSalarySum);
            }
        }
        return result;
    }

}
