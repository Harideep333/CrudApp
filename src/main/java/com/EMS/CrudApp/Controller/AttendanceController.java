package com.EMS.CrudApp.Controller;

import com.EMS.CrudApp.Service.AttendanceService;
import com.EMS.CrudApp.Service.UserService;
import com.EMS.CrudApp.entity.Attendance;
import com.EMS.CrudApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;



@Controller
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/clockin")
    public String clockIn(Authentication authentication, Model model, RedirectAttributes redirectAttributes) {

        String username = authentication.getName();
        try {
            attendanceService.clockIn(username);
            redirectAttributes.addFlashAttribute("message", "Clocked in successfully.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", "You are already clocked in.");
        }
        return "redirect:/employee-dashboard";
    }

    @PostMapping("/clockout")
    public String clockOut(Authentication authentication, Model model) {
        String username = authentication.getName();
        try {
            attendanceService.clockOut(username);  // Call the service method to clock out
            model.addAttribute("message", "You have successfully clocked out!");
        } catch (IllegalStateException ex) {
            model.addAttribute("error", ex.getMessage());  // Handle error and show message
        }
        return "redirect:/employee-dashboard?clockedOut=true";  // Redirect to employee dashboard
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        // Assuming you have a username from authentication
        String username = authentication.getName();
        // Add any necessary attributes, e.g. first name, attendance status, etc.
        model.addAttribute("firstName", username); // You can replace with actual first name logic
        return "employee-dashboard"; // This should resolve to dashboard.html in your templates directory
    }

    @GetMapping("/timesheet")
    public String viewTimesheet(Model model, Authentication authentication) {
        String username = authentication.getName();
        List<Attendance> attendanceList = attendanceService.getAttendanceForUser(username);
        model.addAttribute("attendanceList", attendanceList);
        return "timesheet"; // Make sure timesheet.html exists in templates
    }

    @GetMapping("/manager/attendance")
    @PreAuthorize( "hasRole('MANAGER')")
    public String viewAllTimesheet(Model model, Authentication authentication) {
        List<Attendance> attendanceList = attendanceService.getAllAttendance();
        model.addAttribute("attendanceList", attendanceList);
        return "manager-attendance";
    }
}