package com.EMS.CrudApp.Controller;

import com.EMS.CrudApp.Service.AttendanceService;
import com.EMS.CrudApp.Service.TaskService;
import com.EMS.CrudApp.Service.UserService;
import com.EMS.CrudApp.entity.Attendance;
import com.EMS.CrudApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Harideep Reddy Boothpur
 * @created 4/27/25 10:23 PM
 */

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AttendanceService attendanceService;



    @RequestMapping("/employee-dashboard")
    public String showEmployeeDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("firstName", user.getFirstName());

        // Check if the user is clocked in or not
        boolean isClockedIn = attendanceService.isClockedIn(username);  // Add a method to check this
        model.addAttribute("isClockedIn", isClockedIn);

        return "employee-dashboard";
    }

    @RequestMapping("/manager-dashboard")
    public String showManagerDashboard(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("firstName", user.getFirstName());
        return "manager-dashboard";
    }



    @RequestMapping("/profile")
    public String showProfile(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String username = auth.getName(); // Get the username
            User user = userService.findByUsername(username); // Get the user data from service
            model.addAttribute("user", user); // Add user data to the model

            String dashboardUrl = "";
            if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))) {
                dashboardUrl = "/employee-dashboard";
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) {
                dashboardUrl = "/manager-dashboard";
            }
            model.addAttribute("dashboardUrl", dashboardUrl); // Add the URL to the model
        }
        return "profile";

    }

    @GetMapping("/employee/directory")
    @PreAuthorize("hasRole('MANAGER')")
    public String showEmployeeDirectory(Model model, Authentication auth){
        String username = auth.getName();
        User manager = userService.findByUsername(username);
        List<User> employees = userService.findAllUsers().stream()
                .filter(user -> !user.getId().equals(manager.getId()))
                .toList();
        model.addAttribute("employees", employees);
        return "employee-directory";


    }


    @GetMapping("/profile/edit")
    public String showEditProfileForm(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "edit-profile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute("user") User updatedUser, Authentication authentication) {
        String username = authentication.getName();
        User existingUser = userService.findByUsername(username);

        // Update fields
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhone(updatedUser.getPhone());
        userService.save(existingUser);

        return "redirect:/profile";
    }


    @DeleteMapping("/employee/directory/delete/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public String deleteEmployee(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/employee/directory";
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('MANAGER')")
    public String registerEmployee(@ModelAttribute User newUser, Model model) {
        userService.saveUser(newUser);
        return "redirect:/employee/directory";
    }



   @GetMapping("/register")
    public String showRegisterForm(Model model, Authentication auth) {
        model.addAttribute("user", new User());
        if (auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) {
            model.addAttribute("actionUrl", "/register"); // manager submits here
       } else {
            model.addAttribute("actionUrl", "/register/save"); // normal user
        }
        return "register";
    }

    @GetMapping("/manager/add-employee")
    @PreAuthorize("hasRole('MANAGER')")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("user", new User());
        return "manager-add-employee";
    }

    @PostMapping("/manager/add-employee")
    @PreAuthorize("hasRole('MANAGER')")
    public String saveEmployee(User user) {
        userService.saveManagerAddedEmployee(user);  // You’ll define this method
        return "redirect:/employee/directory";  // Redirect to employee directory after adding
    }






}



