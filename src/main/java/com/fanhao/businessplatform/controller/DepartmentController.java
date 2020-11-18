package com.fanhao.businessplatform.controller;

import com.fanhao.businessplatform.common.CommonResult;
import com.fanhao.businessplatform.entity.BO.DepartmentBO;
import com.fanhao.businessplatform.entity.Department;
import com.fanhao.businessplatform.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller("department")
@RequestMapping(value = "/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String departmentPage() {
        return "/department/departmentlist";
    }

    @RequestMapping(value = "/adddepartment")
    public String addDepartmentPage() {
        return "/department/addDepartment";
    }

    @RequestMapping(value = "/editdepartment")
    public String addDepartment(Integer id,
                                Model model) {
        DepartmentBO departmentBO = departmentService.selectDepartmentById(id).getData();
        model.addAttribute("department_info", departmentBO);
        return "/department/editDepartment";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<DepartmentBO>> getAllDepartment(@RequestParam Integer page,
                                                             @RequestParam Integer limit) {
        CommonResult<List<DepartmentBO>> commonResult = departmentService.selectAllDepartment(page, limit);
        return commonResult;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<String> addOrUpdateDepartment(Integer id,
                                                      String departmentName,
                                                      String departmentRegion,
                                                      String description,
                                                      Integer departmentLeader) {
        return departmentService.addOrUpdateDepartment(id, departmentName, departmentRegion, description, departmentLeader);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<String> delete(Integer id) {
        return departmentService.deleteDepartment(id);
    }
}
