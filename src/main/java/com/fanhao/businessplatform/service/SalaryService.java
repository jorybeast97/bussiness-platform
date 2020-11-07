package com.fanhao.businessplatform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.entity.Salary;
import com.fanhao.businessplatform.mapper.SalaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaryService {

    @Autowired
    private SalaryMapper salaryMapper;

    public boolean addSalary(final Salary salary) {
        return salaryMapper.insert(salary) > 0;
    }

    public boolean deleteSalary(final Integer id) {
        return salaryMapper.deleteById(id) > 0;
    }

    public boolean updateSalary(final Salary salary) {
        return salaryMapper.updateById(salary) > 0;
    }

    public Salary selectSalaryById(final Integer id) {
        return salaryMapper.selectById(id);
    }

    public List<Salary> selectAllSalary(final Integer pageNum,
                                        final Integer pageSize) {
        IPage<Salary> page = new Page<>(pageNum, pageSize);
        page = salaryMapper.selectPage(page, null);
        return page.getRecords();
    }
}
