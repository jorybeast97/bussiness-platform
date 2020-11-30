package com.fanhao.businessplatform.entity.BO;

import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.entity.Message;
import lombok.Data;

import java.util.Date;

@Data
public class MessageBO {
    private Integer id;

    private Employee sender;

    private Employee receiver;

    private String title;

    private String content;

    private Date createTime;

    private Boolean status;

    public MessageBO(final Message message,
                     final Employee sender,
                     final Employee receiver) {
        this.id = message.getId();
        this.sender = sender;
        this.receiver = receiver;
        this.title = message.getTitle();
        this.content = message.getContent();
        this.createTime = message.getCreateTime();
        this.status = message.getStatus();
    }
}
