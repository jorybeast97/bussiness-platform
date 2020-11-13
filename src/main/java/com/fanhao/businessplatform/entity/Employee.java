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
}
