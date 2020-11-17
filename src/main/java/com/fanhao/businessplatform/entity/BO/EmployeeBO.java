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
        this.username = employee.getUsername();
        this.password = employee.getPassword();
        this.name = employee.getName();
        this.address = employee.getAddress();
        this.gender = employee.isGender();
        this.phone = employee.getPhone();
        this.email = employee.getEmail();
        this.department = department;
        this.position = employee.getPosition();
        this.role = employee.getRole();
        this.birthday = employee.getBirthday();
        this.idCard = employee.getIdCard();
        this.school = employee.getSchool();
        this.contractStartDate = employee.getContractStartDate();
        this.quitDate = employee.getQuitDate();
        this.workAge = employee.getWorkAge();
        this.remark = employee.getRemark();
    }
}
