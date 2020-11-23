package com.fanhao.businessplatform.service;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.common.constant.ResultStatus;
import com.fanhao.businessplatform.enhance.annotation.Operation;
import com.fanhao.businessplatform.entity.BO.SalaryBO;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.entity.Salary;
import com.fanhao.businessplatform.mapper.EmployeeMapper;
import com.fanhao.businessplatform.mapper.SalaryMapper;
import com.fanhao.businessplatform.utils.CommonUtils;
import com.fanhao.businessplatform.utils.HttpUtils;
import com.fanhao.businessplatform.utils.PermissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SalaryService {

    @Autowired
    private SalaryMapper salaryMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    public boolean addSalary(final Salary salary) {
        return salaryMapper.insert(salary) > 0;
    }

    /**
     * 获取个人薪资折线图信息
     * @param request
     * @param response
     * @return
     */
    public Map<String, List<String>> getPersonalSalaryLineChart(final HttpServletRequest request,
                                                                final HttpServletResponse response) {
        String token = HttpUtils.getCookie(request, PermissionUtils.TOKEN);
        String username = PermissionUtils.getClaimsInformation(token).get(PermissionUtils.JWT_TOKEN_USERNAME);
        Employee employee = employeeMapper.
                selectOne(new QueryWrapper<Employee>().eq(PermissionUtils.JWT_TOKEN_USERNAME, username));
        QueryWrapper<Salary> salaryQueryWrapper = new QueryWrapper<>();
        salaryQueryWrapper.eq("employee_id", employee.getId()).orderByDesc();
        //获取8条
        IPage<Salary> page = new Page<>(1, 8);
        List<Salary> salaryList = salaryMapper.selectPage(page, salaryQueryWrapper).getRecords();
        List<String> salarys = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        salaryList.forEach(salary -> {
            salarys.add(String.valueOf(salary.getTotalSalary()));
            dates.add(CommonUtils.parseStringFromDate(salary.getGrantDate(),"MM-dd"));
        });
        Map<String, List<String>> result = new HashMap<>();
        result.put("salary", salarys);
        result.put("date", dates);
        return result;
    }


    /**
     * 获取个人薪资饼状图信息
     *
     * @param request
     * @param response
     * @return
     */
    public List<Map<String, String>> getPersonalSalaryPieChart(final HttpServletRequest request,
                                                               final HttpServletResponse response) {
        String token = HttpUtils.getCookie(request, PermissionUtils.TOKEN);
        String username = PermissionUtils.getClaimsInformation(token).get(PermissionUtils.JWT_TOKEN_USERNAME);
        Employee employee = employeeMapper.
                selectOne(new QueryWrapper<Employee>().eq(PermissionUtils.JWT_TOKEN_USERNAME, username));
        QueryWrapper<Salary> salaryQueryWrapper = new QueryWrapper<>();
        salaryQueryWrapper.orderByDesc("grant_date").eq("employee_id", employee.getId());
        List<Salary> salaries = salaryMapper.selectList(salaryQueryWrapper);
        Salary leastSalary = salaries.get(0);
        Map<String, String> baseSalary = new HashMap<>();
        baseSalary.put("name", "基础薪资");
        baseSalary.put("value", String.valueOf(leastSalary.getBaseSalary()));
        Map<String, String> bonus = new HashMap<>();
        bonus.put("name", "奖金");
        bonus.put("value", String.valueOf(leastSalary.getBonus()));
        Map<String, String> mealSubsidy = new HashMap<>();
        mealSubsidy.put("name", "餐饮补助");
        mealSubsidy.put("value", String.valueOf(leastSalary.getMealSubsidy()));
        Map<String, String> trafficSubsidy = new HashMap<>();
        trafficSubsidy.put("name", "交通补助");
        trafficSubsidy.put("value", String.valueOf(leastSalary.getTrafficSubsidy()));
        Map<String, String> rentSubsidy = new HashMap<>();
        rentSubsidy.put("name", "房租补助");
        rentSubsidy.put("value", String.valueOf(leastSalary.getRentSubsidy()));
        Map<String, String> additionalSalary = new HashMap<>();
        additionalSalary.put("name", "额外补助");
        additionalSalary.put("value", String.valueOf(leastSalary.getAdditionalSalary()));
        List<Map<String, String>> list = new ArrayList<>();
        list.add(additionalSalary);
        list.add(baseSalary);
        list.add(bonus);
        list.add(mealSubsidy);
        list.add(rentSubsidy);
        list.add(trafficSubsidy);
        return list;
    }

    /**
     * 删除薪资信息
     *
     * @param id
     * @return
     */
    @Operation(operation = "删除薪资信息")
    public CommonResult<String> deleteSalary(final Integer id) {
        salaryMapper.deleteById(id);
        CommonResult<String> commonResult = new CommonResult<>();
        commonResult.setMessage("删除完成");
        commonResult.setCode(ResultStatus.SUCCESS.getResultCode());
        return commonResult;
    }

    /**
     * 获取登陆人的个人薪资信息
     *
     * @param request
     * @param response
     * @param pageNum
     * @param pageSize
     * @return
     */
    public CommonResult<List<SalaryBO>> getPersonalSalaryInformation(final HttpServletRequest request,
                                                                     final HttpServletResponse response,
                                                                     Integer pageNum,
                                                                     Integer pageSize) {
        String token = HttpUtils.getCookie(request, PermissionUtils.TOKEN);
        String username = PermissionUtils.getClaimsInformation(token).get(PermissionUtils.JWT_TOKEN_USERNAME);
        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
        employeeQueryWrapper.eq("username", username);
        Employee employee = employeeMapper.selectOne(employeeQueryWrapper);
        IPage<Salary> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Salary> salaryQueryWrapper = new QueryWrapper<>();
        salaryQueryWrapper.eq("employee_id", employee.getId()).orderByDesc("grant_date");
        page = salaryMapper.selectPage(page, salaryQueryWrapper);
        List<Salary> list = page.getRecords();
        List<SalaryBO> salaryBOS = new ArrayList<>();
        list.forEach(salary -> {
            salaryBOS.add(generateSalaryBO(salary));
        });
        CommonResult<List<SalaryBO>> commonResult = new CommonResult<>();
        commonResult.setData(salaryBOS);
        commonResult.setCount(page.getTotal());
        commonResult.setCode(ResultStatus.LAYUI_SUCCESS.getResultCode());
        return commonResult;
    }

    public boolean updateSalary(final Salary salary) {
        return salaryMapper.updateById(salary) > 0;
    }

    public Salary selectSalaryById(final Integer id) {
        return salaryMapper.selectById(id);
    }

    /**
     * 获取所有的薪资信息
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public CommonResult<List<SalaryBO>> selectAllSalary(final Integer pageNum,
                                                        final Integer pageSize) {
        IPage<Salary> page = new Page<>(pageNum, pageSize);
        page = salaryMapper.selectPage(page, null);
        List<SalaryBO> salaryBOS = new ArrayList<>();
        page.getRecords().forEach(salary -> {
            salaryBOS.add(generateSalaryBO(salary));
        });
        CommonResult<List<SalaryBO>> commonResult = new CommonResult<>();
        commonResult.setData(salaryBOS);
        commonResult.setCount(page.getTotal());
        commonResult.setCode(ResultStatus.LAYUI_SUCCESS.getResultCode());
        return commonResult;
    }

    /**
     * 将Salary生成为一个SalaryBO
     *
     * @param salary
     * @return
     */
    private SalaryBO generateSalaryBO(final Salary salary) {
        Employee employee = employeeMapper.selectById(salary.getEmployeeId());
        SalaryBO salaryBO = new SalaryBO(salary, employee);
        return salaryBO;
    }

    /**
     * 新增或修改薪资信息
     *
     * @param id
     * @param employeeId
     * @param baseSalary
     * @param bonus
     * @param mealSubsidy
     * @param trafficSubsidy
     * @param rentSubsidy
     * @param additionalSalary
     * @param remark
     * @param totalSalary
     * @param grantDate
     * @return
     */
    @Operation(operation = "新增或修改薪资信息")
    public CommonResult<String> addOrUpdateSalary(Integer id,
                                                  Integer employeeId,
                                                  Double baseSalary,
                                                  Double bonus,
                                                  Double mealSubsidy,
                                                  Double trafficSubsidy,
                                                  Integer rentSubsidy,
                                                  Double additionalSalary,
                                                  String remark,
                                                  Double totalSalary,
                                                  String grantDate) {
        Salary salary = generateSalary(id, employeeId, baseSalary, bonus, mealSubsidy, trafficSubsidy, rentSubsidy, additionalSalary, remark, totalSalary, grantDate);
        CommonResult<String> commonResult = new CommonResult<>();
        if (salary.getId() == null) {
            salaryMapper.insert(salary);
            commonResult.setMessage("添加成功");
        } else {
            salaryMapper.updateById(salary);
            commonResult.setMessage("修改成功");
        }
        return commonResult;
    }

    /**
     * 封装Request请求成为一个Salary实体类
     */
    private Salary generateSalary(Integer id,
                                  Integer employeeId,
                                  Double baseSalary,
                                  Double bonus,
                                  Double mealSubsidy,
                                  Double trafficSubsidy,
                                  Integer rentSubsidy,
                                  Double additionalSalary,
                                  String remark,
                                  Double totalSalary,
                                  String grantDate) {
        Salary salary = new Salary();
        salary.setId(id);
        salary.setEmployeeId(employeeId);
        salary.setBaseSalary(baseSalary);
        salary.setBonus(bonus);
        salary.setMealSubsidy(mealSubsidy);
        salary.setTrafficSubsidy(trafficSubsidy);
        salary.setRemark(remark);
        salary.setRentSubsidy(rentSubsidy);
        salary.setAdditionalSalary(additionalSalary);
        salary.setTotalSalary(totalSalary);
        salary.setGrantDate(CommonUtils.parseDateFromString(grantDate));
        return salary;
    }
}
