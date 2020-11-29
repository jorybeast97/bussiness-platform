package com.fanhao.businessplatform.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("attendance")
public class Attendance {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer employeeId;

    private Integer departmentId;

    private Date startTime;

    private Date endTime;

    private Long workTime;

    private Integer status;
}
