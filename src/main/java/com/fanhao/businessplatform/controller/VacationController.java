package com.fanhao.businessplatform.controller;

import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.entity.BO.EmployeeBO;
import com.fanhao.businessplatform.entity.BO.VacationBO;
import com.fanhao.businessplatform.entity.Vacation;
import com.fanhao.businessplatform.service.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller("vacation")
@RequestMapping(value = "/vacation")
public class VacationController {
    @Autowired
    private VacationService vacationService;

    @RequestMapping(value = "")
    public String vacationListPage() {
        return "/vacation/vacationList";
    }

    @RequestMapping(value = "/vacationInfo")
    public String vacationViewInfoPage(Integer id,
                                       Model model) {
        VacationBO vacationBO = vacationService.getVacationBOById(id);
        model.addAttribute("vacationInfo", vacationBO);
        return "/vacation/vacationInfo";
    }

    @RequestMapping(value = "/personalVacation")
    public String personalVacationPage(final HttpServletRequest request,
                                       final HttpServletResponse response,
                                       Model model) {
        return "/vacation/personalVacation";
    }

    @RequestMapping(value = "addVacation")
    public String addVacationPage() {
        return "/vacation/addVacation";
    }

    @RequestMapping(value = "/personalVacationInformation")
    @ResponseBody
    public CommonResult<List<VacationBO>> getPersonalVacationInformation(final HttpServletRequest request,
                                                                         final HttpServletResponse response,
                                                                         Integer page,
                                                                         Integer limit) {
        return vacationService.getPersonalVacationInformation(request, response, page, limit);
    }

    @RequestMapping(value = "/editVacation")
    public String editVacation(Integer id,
                               Model model) {
        VacationBO vacationBO = vacationService.editVacation(id);
        model.addAttribute("vacation", vacationBO);
        return "/vacation/editVacation";
    }

    /**
     * 新增休假任务
     * @param request
     * @param response
     * @param vacationReason
     * @param startTime
     * @param endTime
     * @param remark
     * @return
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public CommonResult<String> addVacation(final HttpServletRequest request,
                                            final HttpServletResponse response,
                                            String vacationReason,
                                            String startTime,
                                            String endTime,
                                            String remark
                                            ) {
        return vacationService.addVacation(request, response, vacationReason, startTime, endTime, remark);
    }

    @RequestMapping(value = "/update")
    @ResponseBody
    public CommonResult<String> updateVacation(final HttpServletRequest request,
                                               final HttpServletResponse response,
                                               Integer id,
                                               String vacationReason,
                                               String startTime,
                                               String endTime,
                                               String remark) {
        return vacationService.updateVacation(request, response, id, vacationReason, startTime, endTime, remark);
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public CommonResult<String> deleteVacation(Integer id) {
        return vacationService.deleteVacation(id);
    }

    @RequestMapping(value = "/userSubordinateVacationList")
    @ResponseBody
    public CommonResult<List<VacationBO>> getUserSubordinateVacationList(final HttpServletRequest request,
                                                                         final HttpServletResponse response,
                                                                         Integer page,
                                                                         Integer limit) {
        return vacationService.getUserSubordinateVacationList(request, response, page, limit);
    }

    @RequestMapping(value = "/updateVacationToPassed")
    @ResponseBody
    public CommonResult<String> updateVacationToPassed(final HttpServletRequest request,
                                                       final HttpServletResponse response,
                                                       Integer id) {
        return vacationService.updateVacationToPassed(request, response, id);
    }

    @RequestMapping(value = "/updateVacationToUnPassed")
    @ResponseBody
    public CommonResult<String> updateVacationToUnPassed(final HttpServletRequest request,
                                                       final HttpServletResponse response,
                                                       Integer id) {
        return vacationService.updateVacationToUnPassed(request, response, id);
    }

}
