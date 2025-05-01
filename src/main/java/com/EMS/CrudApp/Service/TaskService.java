package com.EMS.CrudApp.Service;

import com.EMS.CrudApp.Repository.TaskRepository;
import com.EMS.CrudApp.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Harideep Reddy Boothpur
 * @created 4/28/25 12:51 PM
 */

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks(){
     return taskRepository.findAll();
    }

    public void createTask(Task task){
     taskRepository.save(task);
    }


    public List<Task> getTasksAssignedTo(String username) {
        return taskRepository.findByTaskAssignedTo(username);
    }


    public void markAsFinished(Long taskId, String username) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        if (!task.getTaskAssignedTo().equals(username)) {
            throw new SecurityException("Not allowed to modify this task.");
        }

        task.setTaskStatus("FINISHED");
        taskRepository.save(task);
    }
}


