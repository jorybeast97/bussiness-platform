package com.fanhao.businessplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("exception_log")
public class ExceptionLog {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;

    private String host;

    private String address;

    private String description;

    private Date happenTime;

    private String detail;
}
