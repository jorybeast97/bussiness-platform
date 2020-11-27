package com.fanhao.businessplatform.entity.BO;

import com.fanhao.businessplatform.entity.Department;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.entity.Vacation;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class VacationBO {
    private Integer id;

    private Employee applicant;

    private Department department;

    private String vacationReason;

    private Date startTime;

    private Date endTime;

    private String remark;
    //申请状态,0为待审核，1为同意，2为拒绝
    private Integer approvalStatus;

    private Date createTime;

    public VacationBO(final Employee employee,
                      final Department department,
                      final Vacation vacation) {
        this.id = vacation.getId();
        this.applicant = employee;
        this.department = department;
        this.vacationReason = vacation.getVacationReason();
        this.startTime = vacation.getStartTime();
        this.endTime = vacation.getEndTime();
        this.remark = vacation.getRemark();
        this.approvalStatus = vacation.getApprovalStatus();
        this.createTime = vacation.getCreateTime();
    }
}
