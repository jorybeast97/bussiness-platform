package com.fanhao.businessplatform.entity.BO;

import com.fanhao.businessplatform.entity.Attendance;
import com.fanhao.businessplatform.entity.Department;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.utils.CommonUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class AttendanceBO {
    private Integer id;

    private Employee employee;

    private Department department;

    private Date startTime;

    private Date endTime;

    private String workTime;

    //0 - 未离岗签退 1 - 工作未满8小时 2 - 工作已满8小时
    private Integer status;

    public AttendanceBO(final Attendance attendance,
                        final Employee employee,
                        final Department department) {
        this.id = attendance.getId();
        this.employee = employee;
        this.department = department;
        this.startTime = attendance.getStartTime();
        this.endTime = attendance.getEndTime();
        if (attendance.getWorkTime() != null){
            this.workTime = CommonUtils.getPeriodFromDate(attendance.getWorkTime());
        }
        this.status = attendance.getStatus();
    }
}
