package com.fanhao.businessplatform.entity.BO;

import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.entity.Salary;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class SalaryBO {
    private Integer id;

    private Employee employee;

    private Double baseSalary;

    private Double bonus;

    private Double mealSubsidy;

    private Double trafficSubsidy;

    private Integer rentSubsidy;

    private Double additionalSalary;

    private String remark;

    private Double totalSalary;

    private Date grantDate;

    public SalaryBO(final Salary salary,
                    final Employee employee) {
        this.id = salary.getId();
        this.employee = employee;
        this.baseSalary = salary.getBaseSalary();
        this.bonus = salary.getBonus();
        this.mealSubsidy = salary.getMealSubsidy();
        this.trafficSubsidy = salary.getTrafficSubsidy();
        this.rentSubsidy = salary.getRentSubsidy();
        this.additionalSalary = salary.getAdditionalSalary();
        this.remark = salary.getRemark();
        this.totalSalary = salary.getTotalSalary();
        this.grantDate = salary.getGrantDate();
    }
}
