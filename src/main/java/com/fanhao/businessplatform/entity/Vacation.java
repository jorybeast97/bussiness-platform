package com.fanhao.businessplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("vacation")
public class Vacation {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer applicant;

    private Integer departmentId;

    private String vacationReason;

    private Date startTime;

    private Date endTime;

    private String remark;
    //申请状态,默认false
    private Integer approvalStatus;

    private Date createTime;
}
