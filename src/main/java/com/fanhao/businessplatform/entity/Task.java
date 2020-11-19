package com.fanhao.businessplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fanhao.businessplatform.utils.CommonUtils;
import lombok.Data;
import sun.dc.pr.PRError;

import java.util.Date;

@Data
@TableName("task")
public class Task {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String taskName;

    private String taskContent;

    private Integer sender;

    private Integer receiver;

    private Integer taskStatus;

    private String remark;

    private Date createDate;

    private Date endDate;

    public Task(Integer id, String taskName, String taskContent, Integer sender, Integer receiver, Integer taskStatus, String remark, Date createDate, Date endDate) {
        this.id = id;
        this.taskName = taskName;
        this.taskContent = taskContent;
        this.sender = sender;
        this.receiver = receiver;
        this.taskStatus = taskStatus;
        this.remark = remark;
        this.createDate = createDate;
        this.endDate = endDate;
    }

    public Task(Integer id, String taskName, String taskContent, Integer sender, Integer receiver, Integer taskStatus, String remark, String createDate, String endDate) {
        this.id = id;
        this.taskName = taskName;
        this.taskContent = taskContent;
        this.sender = sender;
        this.receiver = receiver;
        this.taskStatus = taskStatus;
        this.remark = remark;
        this.createDate = CommonUtils.parseDateFromString(createDate);
        this.endDate = CommonUtils.parseDateFromString(endDate);
    }
}
