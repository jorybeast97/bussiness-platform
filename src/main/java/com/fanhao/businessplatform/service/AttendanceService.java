package com.fanhao.businessplatform.service;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.common.constant.ResultStatus;
import com.fanhao.businessplatform.entity.Attendance;
import com.fanhao.businessplatform.entity.BO.AttendanceBO;
import com.fanhao.businessplatform.entity.BO.EmployeeBO;
import com.fanhao.businessplatform.entity.Department;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.mapper.AttendanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service("attendanceService")
public class AttendanceService {
    public static final long EVERY_DAY_WORK_TIME = 1000 * 60 * 60 * 8;
    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    /**
     * 获取部门考勤信息
     * @param request
     * @param response
     * @param pageNum
     * @param pageSize
     * @return
     */
    public CommonResult<List<AttendanceBO>> getDepartmentAttendanceBOList(final HttpServletRequest request,
                                                                          final HttpServletResponse response,
                                                                          Integer pageNum,
                                                                          Integer pageSize) {
        Employee employee = employeeService.getCurrentlyLoggedInUser(request, response);
        IPage<Attendance> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Attendance> wrapper = new QueryWrapper<>();
        wrapper.eq("department_id", employee.getDepartment()).orderByDesc("start_time");
        List<Attendance> list = attendanceMapper.selectPage(page, wrapper).getRecords();
        List<AttendanceBO> data = new ArrayList<>();
        list.forEach(attendance -> {
            data.add(generateAttendanceBO(attendance));
        });
        CommonResult<List<AttendanceBO>> commonResult = new CommonResult<>();
        commonResult.setData(data);
        commonResult.setCode(ResultStatus.LAYUI_SUCCESS.getResultCode());
        commonResult.setCount(page.getTotal());
        return commonResult;
    }

    /**
     * 获取个人考勤信息
     * @param request
     * @param response
     * @param pageNum
     * @param pageSize
     * @return
     */
    public CommonResult<List<AttendanceBO>> getPersonalAttendanceBOList(final HttpServletRequest request,
                                                                          final HttpServletResponse response,
                                                                          Integer pageNum,
                                                                          Integer pageSize) {
        Employee employee = employeeService.getCurrentlyLoggedInUser(request, response);
        IPage<Attendance> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Attendance> wrapper = new QueryWrapper<>();
        wrapper.eq("department_id", employee.getDepartment())
                .eq("employee_id",employee.getId())
                .orderByDesc("start_time");
        List<Attendance> list = attendanceMapper.selectPage(page, wrapper).getRecords();
        List<AttendanceBO> data = new ArrayList<>();
        list.forEach(attendance -> {
            data.add(generateAttendanceBO(attendance));
        });
        CommonResult<List<AttendanceBO>> commonResult = new CommonResult<>();
        commonResult.setData(data);
        commonResult.setCode(ResultStatus.LAYUI_SUCCESS.getResultCode());
        commonResult.setCount(page.getTotal());
        return commonResult;
    }

    /**
     * 获取考勤不合格记录
     * @param request
     * @param response
     * @param pageNum
     * @param pageSize
     * @return
     */
    public CommonResult<List<AttendanceBO>> getUnqualifiedDepartmentAttendanceBOList(final HttpServletRequest request,
                                                                        final HttpServletResponse response,
                                                                        Integer pageNum,
                                                                        Integer pageSize) {
        Employee employee = employeeService.getCurrentlyLoggedInUser(request, response);
        IPage<Attendance> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Attendance> wrapper = new QueryWrapper<>();
        wrapper.eq("department_id", employee.getDepartment())
                .orderByDesc("start_time");
        List<Attendance> list = attendanceMapper.selectPage(page, wrapper).getRecords();
        List<AttendanceBO> data = new ArrayList<>();
        list.forEach(attendance -> {
            if (attendance.getStatus() != 2) {
                data.add(generateAttendanceBO(attendance));
            }
        });
        CommonResult<List<AttendanceBO>> commonResult = new CommonResult<>();
        commonResult.setData(data);
        commonResult.setCode(ResultStatus.LAYUI_SUCCESS.getResultCode());
        commonResult.setCount(page.getTotal());
        return commonResult;
    }

    /**
     * 获取当月员工工时
     * @param request
     * @param response
     * @return
     */
    public CommonResult<List<AttendanceBO>> getCurrentMonthWorkTimeRank(final HttpServletRequest request,
                                                                        final HttpServletResponse response,
                                                                        Integer pageNum,
                                                                        Integer pageSize) {
        Employee employee = employeeService.getCurrentlyLoggedInUser(request, response);
        QueryWrapper<Attendance> wrapper = new QueryWrapper<>();
        wrapper.eq("department_id", employee.getDepartment())
                .between("start_time", DateUtil.beginOfMonth(new Date()), new Date());
        List<Attendance> attendances = attendanceMapper.selectList(wrapper);
        //用Employee_id作为Key，获取每个员工当月工时数量
        Map<Integer, Attendance> map = new HashMap<>();
        attendances.forEach(attendance -> {
            if (map.get(attendance.getEmployeeId()) == null){
                map.put(attendance.getEmployeeId(), attendance);
            }else {
                Attendance pre = map.get(attendance.getEmployeeId());
                if (attendance.getWorkTime() != null) {
                    pre.setWorkTime(pre.getWorkTime() + attendance.getWorkTime());
                }
                map.put(pre.getEmployeeId(), pre);
            }
        });
        Collection<Attendance> collection = map.values();
        attendances = new ArrayList<>(collection);
        List<AttendanceBO> data = new ArrayList<>();
        attendances.forEach(attendance -> {
            data.add(generateAttendanceBO(attendance));
        });
        CommonResult<List<AttendanceBO>> commonResult = new CommonResult<>();
        commonResult.setCount((long) data.size());
        commonResult.setData(data);
        commonResult.setCode(ResultStatus.LAYUI_SUCCESS.getResultCode());
        return commonResult;
    }



    /**
     * 到岗签到功能
     * @param request
     * @param response
     * @return
     */
    public CommonResult<String> signIn(final HttpServletRequest request,
                                       final HttpServletResponse response) {
        CommonResult<String> commonResult = new CommonResult<>();
        //获取当前用户
        Employee employee = employeeService.getCurrentlyLoggedInUser(request, response);
        if (isSignInToday(employee)){
            commonResult.setMessage("今日已经完成到岗签到");
            commonResult.setCode(ResultStatus.SUCCESS.getResultCode());
            return commonResult;
        }
        Attendance attendance = new Attendance();
        attendance.setEmployeeId(employee.getId());
        attendance.setDepartmentId(employee.getDepartment());
        attendance.setStartTime(new Date());
        attendance.setStatus(0);
        attendanceMapper.insert(attendance);
        commonResult.setMessage("到岗签到完成");
        commonResult.setCode(ResultStatus.SUCCESS.getResultCode());
        return commonResult;
    }

    /**
     * 离岗签退功能
     * @param request
     * @param response
     * @return
     */
    public CommonResult<String> signOut(final HttpServletRequest request,
                                        final HttpServletResponse response) {
        CommonResult<String> commonResult = new CommonResult<>();
        Employee employee = employeeService.getCurrentlyLoggedInUser(request, response);
        if (!isSignInToday(employee)) {
            commonResult.setMessage("今日还未签到,无法签退");
            commonResult.setCode(ResultStatus.SUCCESS.getResultCode());
            return commonResult;
        }
        QueryWrapper<Attendance> wrapper = new QueryWrapper<>();
        wrapper.eq("employee_id", employee.getId())
                .between("start_time", DateUtil.beginOfDay(new Date()), DateUtil.endOfDay(new Date()));
        Attendance attendance = attendanceMapper.selectOne(wrapper);
        attendance.setEndTime(new Date());
        long period = attendance.getEndTime().getTime() - attendance.getStartTime().getTime();
        attendance.setWorkTime(period);
        //判断状态
        if (period > AttendanceService.EVERY_DAY_WORK_TIME){
            attendance.setStatus(2);
        }else {
            attendance.setStatus(1);
        }
        attendanceMapper.updateById(attendance);
        commonResult.setMessage("离岗签退成功");
        commonResult.setCode(ResultStatus.SUCCESS.getResultCode());
        return commonResult;
    }

    /**
     * 申城AttendanceBO
     * @param attendance
     * @return
     */
    public AttendanceBO generateAttendanceBO(final Attendance attendance) {
        Employee employee = employeeService.selectEmployeeById(attendance.getEmployeeId());
        EmployeeBO employeeBO = employeeService.generateEmployeeBO(employee);
        return new AttendanceBO(attendance, employee, employeeBO.getDepartment());
    }

    /**
     * 检查员工今天是否已经到岗签到
     * @return
     */
    private boolean isSignInToday(final Employee employee) {
        Date start = DateUtil.beginOfDay(new Date());
        Date end = DateUtil.endOfDay(new Date());
        QueryWrapper<Attendance> wrapper = new QueryWrapper<>();
        wrapper.eq("employee_id",employee.getId())
                .between("start_time", start, end);
        Attendance attendance = attendanceMapper.selectOne(wrapper);
        return attendance != null;
    }
}
