package com.fanhao.businessplatform.controller;

import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.entity.BO.SalaryBO;
import com.fanhao.businessplatform.entity.Salary;
import com.fanhao.businessplatform.service.PermissionService;
import com.fanhao.businessplatform.service.SalaryService;
import com.fanhao.businessplatform.utils.PermissionUtils;
import javafx.scene.chart.ValueAxis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller("salary")
@RequestMapping(value = "/salary")
public class SalaryController {

    @Autowired
    private SalaryService salaryService;

    @Autowired
    private PermissionService permissionService;

    @RequestMapping(value = "")
    public String salaryListPage(final HttpServletRequest request,
                                 final HttpServletResponse response,
                                 Model model) {
        return "/salary/salaryList";
    }

    @RequestMapping(value = "/add")
    public String addSalary() {
        return "/salary/addSalary";
    }

    @RequestMapping(value = "/edit")
    public String editSalary(Integer id,
                             Model model) {
        Salary salary = salaryService.selectSalaryById(id);
        model.addAttribute("salaryInfo", salary);
        return "/salary/editSalary";
    }

    @RequestMapping(value = "/personalsalary")
    public String personalSalaryPage(final HttpServletRequest request,
                                     final HttpServletResponse response,
                                     Model model) {
        return "/salary/personalSalary";
    }

    @RequestMapping(value = "/personalSalaryLineChart")
    @ResponseBody
    public Map<String, List<String>> getPersonalSalaryLineChart(final HttpServletRequest request,
                                                                final HttpServletResponse response) {
        return salaryService.getPersonalSalaryLineChart(request, response);
    }

    @RequestMapping(value = "/personalSalaryPieChart")
    @ResponseBody
    public List<Map<String, String>> getPersonalSalaryPieChart(final HttpServletRequest request,
                                                               final HttpServletResponse response) {
        return salaryService.getPersonalSalaryPieChart(request, response);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SalaryBO>> getSalaryBOList(Integer page,
                                                        Integer limit) {
        return salaryService.selectAllSalary(page, limit);
    }

    @RequestMapping(value = "/personallist", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SalaryBO>> getPersonalSalaryBOList(final HttpServletRequest request,
                                                        final HttpServletResponse response,
                                                        Integer page,
                                                        Integer limit) {
        return salaryService.getPersonalSalaryInformation(request, response, page, limit);
    }



    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<String> saveAndUpdateSalary(Integer id,
                                                    Integer employeeId,
                                                    Double baseSalary,
                                                    Double bonus,
                                                    Double mealSubsidy,
                                                    Double trafficSubsidy,
                                                    Integer rentSubsidy,
                                                    Double additionalSalary,
                                                    String remark,
                                                    Double totalSalary,
                                                    String grantDate) {
        return salaryService.addOrUpdateSalary(id, employeeId, baseSalary, bonus, mealSubsidy, trafficSubsidy, rentSubsidy, additionalSalary, remark, totalSalary, grantDate);
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public CommonResult<String> delete(Integer id) {
        return salaryService.deleteSalary(id);
    }
}
