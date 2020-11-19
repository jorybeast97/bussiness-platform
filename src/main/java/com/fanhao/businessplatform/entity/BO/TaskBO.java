package com.fanhao.businessplatform.entity.BO;

import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.entity.Task;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 1:未开始 2:准备中 3:正在进行 4:已完成 5:失败
 */
@Data
@NoArgsConstructor
public class TaskBO {
    private Integer id;

    private String taskName;

    private String taskContent;

    private Employee sender;

    private Employee receiver;

    private Integer taskStatus;

    private String remark;

    private Date createDate;

    private Date endDate;

    public TaskBO(final Task task,
                  final Employee sender,
                  final Employee receiver) {
        this.sender = sender;
        this.receiver = receiver;
        id = task.getId();
        taskName = task.getTaskName();
        taskContent = task.getTaskContent();
        taskStatus = task.getTaskStatus();
        remark = task.getRemark();
        createDate = task.getCreateDate();
        endDate = task.getEndDate();
    }
}
