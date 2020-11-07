package com.fanhao.businessplatform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanhao.businessplatform.entity.Department;
import com.fanhao.businessplatform.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    public boolean addDepartment(final Department department) {
        return departmentMapper.insert(department) > 0;
    }

    public boolean deleteDepartment(final Integer id) {
        return departmentMapper.deleteById(id) > 0;
    }

    public boolean updateDepartment(final Department department) {
        return departmentMapper.updateById(department) > 0;
    }

    public Department selectDepartmentById(final Integer id) {
        return departmentMapper.selectById(id);
    }

    public List<Department> selectAllDepartment(final int pageNum,
                                                final int pageSize) {
        IPage<Department> page = new Page<>(pageNum, pageSize);
        page = departmentMapper.selectPage(page, null);
        return page.getRecords();
    }
}
