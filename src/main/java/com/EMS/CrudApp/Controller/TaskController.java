package com.EMS.CrudApp.Controller;

import com.EMS.CrudApp.Service.TaskService;
import com.EMS.CrudApp.Service.UserService;
import com.EMS.CrudApp.entity.Task;
import com.EMS.CrudApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

/**
 * @author Harideep Reddy Boothpur
 * @created 4/28/25 7:27 PM
 */
@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @GetMapping("/create")
    public String showCreateTaskForm(Model model, Principal principal) {
        List<User> employees = userService.getAllUsers();
        if (employees == null || employees.isEmpty()) {
            // Handle if no employees found, or send an error message
            model.addAttribute("error", "No employees found!");
        }
        model.addAttribute("employees", employees);
        model.addAttribute("task", new Task());
        String username = principal.getName();
        User user = userService.findByUsername(username);
        boolean isManager = user.getRoles().stream().anyMatch(role -> role.equals("ROLE_MANAGER"));
        model.addAttribute("role", isManager ? "MANAGER" : "EMPLOYEE");

        // Ensure a task object is also added to the model
        return "task"; // Ensure the "task.html" is the correct template
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute Task task) {
        taskService.createTask(task);
        return "redirect:/manager-dashboard";
    }

    // TaskController.java

    @GetMapping("/assigned")
    public String showAssignedTasks(Model model, Authentication authntication) {
        String username = authntication.getName(); // Get the logged-in username
        User user = userService.findByUsername(username);
        List<Task> tasks;
        if (user.getRoles().contains("MANAGER")) {
            tasks = taskService.getAllTasks();  // manager sees all tasks
        } else {
            tasks = taskService.getTasksAssignedTo(username); // employee sees only their tasks
        }
        model.addAttribute("tasks", tasks);
        return "view-tasks"; // make sure your HTML file is named employee_tasks.html
    }


    @GetMapping("")
    public String redirectToAssignedTasks() {
        return "redirect:/tasks/assigned";
    }

    @PostMapping("/mark-finished/{id}")
    public String markAsFinished(@PathVariable("id") Long taskId,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        String username = authentication.getName();

        try {
            taskService.markAsFinished(taskId, username);
            redirectAttributes.addFlashAttribute("message", "Task marked as finished.");
        } catch (SecurityException e) {
            redirectAttributes.addFlashAttribute("error", "You are not allowed to modify this task.");
        }

        return "redirect:/tasks";  // Or whatever page lists tasks
    }
}
