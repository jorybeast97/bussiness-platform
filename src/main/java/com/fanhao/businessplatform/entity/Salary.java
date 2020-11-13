package com.fanhao.businessplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "salary")
public class Salary {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer employeeId;

    private double baseSalary;

    private double bonus;

    private double mealSubsidy;

    private double trafficSubsidy;

    private int rentSubsidy;

    private double additionalSalary;

    private String remark;

    private double totalSalary;

    private Date grantDate;
}
