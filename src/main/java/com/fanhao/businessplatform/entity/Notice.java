package com.fanhao.businessplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("notice")
public class Notice {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String noticeTitle;

    private String noticeContent;

    private Integer sponsor;

    private Integer receiver;

    private Date noticeDate;

    private Integer status;
}
