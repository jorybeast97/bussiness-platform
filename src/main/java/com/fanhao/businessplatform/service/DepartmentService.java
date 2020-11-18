package com.fanhao.businessplatform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.common.constant.ResultStatus;
import com.fanhao.businessplatform.entity.BO.DepartmentBO;
import com.fanhao.businessplatform.entity.Department;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.mapper.DepartmentMapper;
import com.fanhao.businessplatform.mapper.EmployeeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("departmentService")
public class DepartmentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentService.class);
    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    public CommonResult<String> addOrUpdateDepartment(Integer id,
                                                      String departmentName,
                                                      String departmentRegion,
                                                      String description,
                                                      Integer departmentLeader) {
        Department department = generateDepartment(id, departmentName, departmentRegion, description, departmentLeader);
        CommonResult<String> commonResult = new CommonResult<>();
        if (department.getId() == null) {
            departmentMapper.insert(department);
            commonResult.setMessage("新增成功");
        }else {
            departmentMapper.updateById(department);
            commonResult.setMessage("更新成功");
        }
        return commonResult;
    }

    public CommonResult<String> deleteDepartment(final Integer id) {
        CommonResult<String> commonResult = new CommonResult<>();
        if (id == null) {
            commonResult.setMessage("员工不存在");
            commonResult.setCode(ResultStatus.FAILED.getResultCode());
        }
        else {
            departmentMapper.deleteById(id);
            commonResult.setMessage("删除成功");
            commonResult.setCode(ResultStatus.SUCCESS.getResultCode());
        }
        return commonResult;
    }

    public boolean updateDepartment(final Department department) {
        return departmentMapper.updateById(department) > 0;
    }


    public CommonResult<DepartmentBO> selectDepartmentById(final Integer id) {
        DepartmentBO departmentBO = generateDepartmentBO(departmentMapper.selectById(id));
        CommonResult<DepartmentBO> commonResult = new CommonResult<>();
        commonResult.setCode(ResultStatus.LAYUI_SUCCESS.getResultCode());
        commonResult.setMessage(ResultStatus.LAYUI_SUCCESS.getStatus());
        commonResult.setData(departmentBO);
        return commonResult;
    }

    public CommonResult<List<DepartmentBO>> selectAllDepartment(final int pageNum,
                                                final int pageSize) {
        IPage<Department> page = new Page<>(pageNum, pageSize);
        page = departmentMapper.selectPage(page, null);
        List<Department> departmentList = page.getRecords();
        List<DepartmentBO> departmentBOList = new ArrayList<>();
        departmentList.forEach(department -> {
            DepartmentBO departmentBO = generateDepartmentBO(department);
            departmentBOList.add(departmentBO);
        });
        CommonResult<List<DepartmentBO>> commonResult = new CommonResult<>(departmentBOList,
                ResultStatus.LAYUI_SUCCESS.getResultCode(), ResultStatus.LAYUI_SUCCESS.getStatus(), page.getTotal());
        return commonResult;
    }

    public DepartmentBO generateDepartmentBO(final Department department) {
        if (department == null) return null;
        if (department.getDepartmentLeader() != null) {
            Employee employee = employeeMapper.selectById(department.getDepartmentLeader());
            return new DepartmentBO(department, employee);
        }
        return new DepartmentBO(department, null);
    }

    public Department generateDepartment(Integer id,
                                         String departmentName,
                                         String departmentRegion,
                                         String description,
                                         Integer departmentLeader) {
        Department department = new Department();
        department.setId(id);
        department.setDepartmentName(departmentName);
        department.setDepartmentRegion(departmentRegion);
        department.setDescription(description);
        department.setDepartmentLeader(departmentLeader);
        return department;
    }
}
