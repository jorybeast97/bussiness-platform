package com.fanhao.businessplatform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.common.constant.ResultStatus;
import com.fanhao.businessplatform.entity.BO.SalaryBO;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.entity.Salary;
import com.fanhao.businessplatform.mapper.EmployeeMapper;
import com.fanhao.businessplatform.mapper.SalaryMapper;
import com.fanhao.businessplatform.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SalaryService {

    @Autowired
    private SalaryMapper salaryMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    public boolean addSalary(final Salary salary) {
        return salaryMapper.insert(salary) > 0;
    }

    public CommonResult<String> deleteSalary(final Integer id) {
        salaryMapper.deleteById(id);
        CommonResult<String> commonResult = new CommonResult<>();
        commonResult.setMessage("删除完成");
        commonResult.setCode(ResultStatus.SUCCESS.getResultCode());
        return commonResult;
    }

    public boolean updateSalary(final Salary salary) {
        return salaryMapper.updateById(salary) > 0;
    }

    public Salary selectSalaryById(final Integer id) {
        return salaryMapper.selectById(id);
    }

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

    private SalaryBO generateSalaryBO(final Salary salary) {
        Employee employee = employeeMapper.selectById(salary.getEmployeeId());
        SalaryBO salaryBO = new SalaryBO(salary, employee);
        return salaryBO;
    }

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
        }else {
            salaryMapper.updateById(salary);
            commonResult.setMessage("修改成功");
        }
        return commonResult;
    }

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
