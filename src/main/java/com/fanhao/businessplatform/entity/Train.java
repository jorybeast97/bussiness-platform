package com.fanhao.businessplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("train")
public class Train {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String employeeId;

    private String trainContent;

    private Date trainDate;

    private String remark;
}
