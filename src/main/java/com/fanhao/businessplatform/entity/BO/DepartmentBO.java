package com.fanhao.businessplatform.entity.BO;

import com.fanhao.businessplatform.entity.Department;
import com.fanhao.businessplatform.entity.Employee;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DepartmentBO {
    private Integer id;

    private String departmentName;

    private String description;

    private Employee departmentLeader;

    private String departmentRegion;

    /**
     * 创建返回数据
     * @param department
     * @param employee
     */
    public DepartmentBO(final Department department, final Employee employee) {
        this.id = department.getId();
        this.departmentName = department.getDepartmentName();
        this.description = department.getDescription();
        this.departmentLeader = employee;
        this.departmentRegion = department.getDepartmentRegion();
    }
}
