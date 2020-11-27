package com.fanhao.businessplatform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.common.constant.ResultStatus;
import com.fanhao.businessplatform.entity.BO.EmployeeBO;
import com.fanhao.businessplatform.entity.BO.VacationBO;
import com.fanhao.businessplatform.entity.Department;
import com.fanhao.businessplatform.entity.Employee;
import com.fanhao.businessplatform.entity.Vacation;
import com.fanhao.businessplatform.mapper.VacationMapper;
import com.fanhao.businessplatform.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("vacationService")
public class VacationService {
    @Autowired
    private VacationMapper vacationMapper;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    public VacationBO getVacationBOById(Integer id) {
        Vacation vacation = vacationMapper.selectById(id);
        VacationBO vacationBO = generateVacationBO(vacation);
        return vacationBO;
    }

    public VacationBO editVacation(Integer id) {
        Vacation vacation = vacationMapper.selectById(id);
        return generateVacationBO(vacation);
    }

    public CommonResult<String> deleteVacation(Integer id) {
        CommonResult<String> commonResult = new CommonResult<>();
        vacationMapper.deleteById(id);
        commonResult.setCode(ResultStatus.SUCCESS.getResultCode());
        commonResult.setMessage("删除成功");
        return commonResult;
    }

    /**
     * 新增任务
     * @param request
     * @param response
     * @param vacationReason
     * @param startTime
     * @param endTime
     * @param remark
     * @return
     */
    public CommonResult<String> addVacation(final HttpServletRequest request,
                                            final HttpServletResponse response,
                                            String vacationReason,
                                            String startTime,
                                            String endTime,
                                            String remark) {
        Employee employee = employeeService.getCurrentlyLoggedInUser(request, response);
        Vacation vacation = new Vacation();
        vacation.setApplicant(employee.getId());
        vacation.setDepartmentId(employee.getDepartment());
        vacation.setVacationReason(vacationReason);
        vacation.setStartTime(CommonUtils.parseDateFromString(startTime));
        vacation.setEndTime(CommonUtils.parseDateFromString(endTime));
        vacation.setRemark(remark);
        vacation.setApprovalStatus(0);
        vacation.setCreateTime(new Date());
        vacationMapper.insert(vacation);
        CommonResult<String> commonResult = new CommonResult<>();
        commonResult.setMessage("操作成功");
        commonResult.setCode(ResultStatus.SUCCESS.getResultCode());
        return commonResult;
    }

    /**
     * 获取个人的请假信息
     * @param request
     * @param response
     * @param pageNum
     * @param pageSize
     * @return
     */
    public CommonResult<List<VacationBO>> getPersonalVacationInformation(final HttpServletRequest request,
                                                                         final HttpServletResponse response,
                                                                         Integer pageNum,
                                                                         Integer pageSize) {
        Employee employee = employeeService.getCurrentlyLoggedInUser(request, response);
        IPage<Vacation> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Vacation> wrapper = new QueryWrapper<>();
        wrapper.eq("applicant", employee.getId()).orderByDesc("create_time");
        List<Vacation> list = vacationMapper.selectPage(page, wrapper).getRecords();
        List<VacationBO> result = new ArrayList<>();
        list.forEach(vacation -> {
            result.add(generateVacationBO(vacation));
        });
        CommonResult<List<VacationBO>> commonResult = new CommonResult<>();
        commonResult.setData(result);
        commonResult.setCode(ResultStatus.LAYUI_SUCCESS.getResultCode());
        commonResult.setCount(page.getTotal());
        return commonResult;
    }

    /**
     * 审批通过更新
     * @param request
     * @param response
     * @param id
     * @return
     */
    public CommonResult<String> updateVacationToPassed(final HttpServletRequest request,
                                                        final HttpServletResponse response,
                                                        Integer id) {
        Vacation vacation = vacationMapper.selectById(id);
        vacation.setApprovalStatus(1);
        vacationMapper.updateById(vacation);
        CommonResult<String> commonResult = new CommonResult<>();
        commonResult.setMessage("操作成功");
        return commonResult;
    }

    /**
     * 审批不通过更新
     * @param request
     * @param response
     * @param id
     * @return
     */
    public CommonResult<String> updateVacationToUnPassed(final HttpServletRequest request,
                                                       final HttpServletResponse response,
                                                       Integer id) {
        Vacation vacation = vacationMapper.selectById(id);
        vacation.setApprovalStatus(2);
        vacationMapper.updateById(vacation);
        CommonResult<String> commonResult = new CommonResult<>();
        commonResult.setMessage("操作成功");
        return commonResult;
    }

    /**
     * 获取当前用户(管理员)部门的请假申请
     * @param request
     * @param response
     * @param pageNum
     * @param pageSize
     * @return
     */
    public CommonResult<List<VacationBO>> getUserSubordinateVacationList(final HttpServletRequest request,
                                                                         final HttpServletResponse response,
                                                                         Integer pageNum,
                                                                         Integer pageSize) {
        //获取当前登录用户
        Employee employee = employeeService.getCurrentlyLoggedInUser(request, response);
        IPage<Vacation> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Vacation> wrapper = new QueryWrapper<>();
        wrapper.eq("department_id", employee.getDepartment()).orderByDesc("create_time");
        List<Vacation> vacationList = vacationMapper.selectPage(page, wrapper).getRecords();
        List<VacationBO> vacationBOList = new ArrayList<>();
        vacationList.forEach(vacation -> {
            vacationBOList.add(generateVacationBO(vacation));
        });
        CommonResult<List<VacationBO>> result = new CommonResult<>();
        result.setCount(page.getTotal());
        result.setData(vacationBOList);
        result.setCode(ResultStatus.LAYUI_SUCCESS.getResultCode());
        return result;
    }

    public VacationBO generateVacationBO(final Employee employee,
                                         final Department department,
                                         final Vacation vacation) {
        return new VacationBO(employee, department, vacation);
    }

    public VacationBO generateVacationBO(final Vacation vacation) {
        Employee employee = employeeService.selectEmployeeById(vacation.getApplicant());
        EmployeeBO employeeBO = employeeService.generateEmployeeBO(employee);
        return new VacationBO(employee, employeeBO.getDepartment(),vacation);
    }
}
