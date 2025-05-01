package com.EMS.CrudApp.Controller;

import com.EMS.CrudApp.Service.UserService;
import com.EMS.CrudApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Harideep Reddy Boothpur
 * @created 4/28/25 1:09 PM
 */

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;


//    @GetMapping("/register")
//    public String showregistrationPage(Model model){
//        model.addAttribute("user", new User());
//        return "register";
//    }


    @PostMapping("/register/save")
    public String registerUser(@ModelAttribute("user") User user){
        userService.saveUser(user);
        return "redirect:/login";

    }






}
