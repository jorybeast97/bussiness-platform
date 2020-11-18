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

    private Double baseSalary;

    private Double bonus;

    private Double mealSubsidy;

    private Double trafficSubsidy;

    private Integer rentSubsidy;

    private Double additionalSalary;

    private String remark;

    private Double totalSalary;

    private Date grantDate;
}
