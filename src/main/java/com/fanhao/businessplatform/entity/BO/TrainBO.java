package com.fanhao.businessplatform.entity.BO;

import com.fanhao.businessplatform.entity.Employee;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TrainBO {
    private Integer id;

    private List<Employee> employeeList;

    private String trainContent;

    private Date trainDate;

    private String remark;
}
