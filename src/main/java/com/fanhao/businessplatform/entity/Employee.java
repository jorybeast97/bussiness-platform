package com.fanhao.businessplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@TableName("employee")
@NoArgsConstructor
public class Employee {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

    private String name;

    private String address;

    private boolean gender;

    private String phone;

    private String email;

    private int department;

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

    public Employee(Integer id, String username, String password, String name, String address, boolean gender, String phone, String email, int department, String position, String role, Date birthday, String idCard, String school, Date contractStartDate, Date quitDate, int workAge, boolean status, String remark) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.department = department;
        this.position = position;
        this.role = role;
        this.birthday = birthday;
        this.idCard = idCard;
        this.school = school;
        this.contractStartDate = contractStartDate;
        this.quitDate = quitDate;
        this.workAge = workAge;
        this.status = status;
        this.remark = remark;
    }
}
