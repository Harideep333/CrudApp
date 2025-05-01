package com.EMS.CrudApp.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Harideep Reddy Boothpur
 * @created 4/27/25 10:17 PM
 */
@Controller
public class LoginController {

    @RequestMapping("/login")
    public String showLoginPage(){
        return "login";
    }

    @RequestMapping("/register")
    public String showRegisterPage(){
        return "register";
    }


}
