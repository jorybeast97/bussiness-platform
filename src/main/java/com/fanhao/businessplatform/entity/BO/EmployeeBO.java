package com.fanhao.businessplatform.entity.BO;

import com.fanhao.businessplatform.entity.Department;
import com.fanhao.businessplatform.entity.Employee;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class EmployeeBO {
    private Integer id;

    private String username;

    private String password;

    private String name;

    private String address;

    private boolean gender;

    private String phone;

    private String email;

    private Department department;

    private String position;

    private String role;

    private Date birthday;

    private String idCard;

    private String school;

    private Date contractStartDate;

    private Date quitDate;

    private int workAge;

    private boolean status;

    private String remark;

    public EmployeeBO(final Employee employee,
                      final Department department) {
        this.id = employee.getId();
        
    }
}
