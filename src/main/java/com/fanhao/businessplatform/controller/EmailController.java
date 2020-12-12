package com.fanhao.businessplatform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("email")
@RequestMapping("/email")
public class EmailController {

    @RequestMapping(value = "")
    public String emailPage() {
        return "/email/email";
    }


}
