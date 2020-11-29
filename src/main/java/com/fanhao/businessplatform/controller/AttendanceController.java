package com.fanhao.businessplatform.controller;

import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.entity.BO.AttendanceBO;
import com.fanhao.businessplatform.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller("attendance")
@RequestMapping(value = "/attendance")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @RequestMapping(value = "/personalAttendance")
    public String personalAttendancePage(final HttpServletRequest request,
                                         final HttpServletResponse response) {
        return "/attendance/personalAttendance";
    }

    @RequestMapping(value = "/departmentAttendance")
    public String departmentAttendancePage(final HttpServletRequest request,
                                         final HttpServletResponse response) {
        return "/attendance/departmentAttendance";
    }

    @RequestMapping(value = "/getDepartmentAttendanceList")
    @ResponseBody
    public CommonResult<List<AttendanceBO>> getDepartmentAttendanceBOList(final HttpServletRequest request,
                                                                          final HttpServletResponse response,
                                                                          Integer page,
                                                                          Integer limit) {
        return attendanceService.getDepartmentAttendanceBOList(request, response, page, limit);
    }

    @RequestMapping(value = "/getPersonalAttendanceList")
    @ResponseBody
    public CommonResult<List<AttendanceBO>> getPersonalAttendanceBOList(final HttpServletRequest request,
                                                                          final HttpServletResponse response,
                                                                          Integer page,
                                                                          Integer limit) {
        return attendanceService.getPersonalAttendanceBOList(request, response, page, limit);
    }

    @RequestMapping(value = "/getUnqualifiedDepartmentAttendanceList")
    @ResponseBody
    public CommonResult<List<AttendanceBO>> getUnqualifiedDepartmentAttendanceBOList(final HttpServletRequest request,
                                                                        final HttpServletResponse response,
                                                                        Integer page,
                                                                        Integer limit) {
        return attendanceService.getUnqualifiedDepartmentAttendanceBOList(request, response, page, limit);
    }

    @RequestMapping(value = "/getCurrentMonthWorkTimeRank")
    @ResponseBody
    public CommonResult<List<AttendanceBO>> getCurrentMonthWorkTimeRank(final HttpServletRequest request,
                                                                                     final HttpServletResponse response,
                                                                                     Integer page,
                                                                                     Integer limit) {
        return attendanceService.getCurrentMonthWorkTimeRank(request, response, page, limit);
    }

    @RequestMapping(value = "/signIn")
    @ResponseBody
    public CommonResult<String> signIn(final HttpServletRequest request,
                                       final HttpServletResponse response) {
        return attendanceService.signIn(request, response);
    }

    @RequestMapping(value = "/signOut")
    @ResponseBody
    public CommonResult<String> signOut(final HttpServletRequest request,
                                        final HttpServletResponse response) {
        return attendanceService.signOut(request, response);
    }
}
