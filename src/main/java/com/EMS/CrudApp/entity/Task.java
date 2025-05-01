package com.EMS.CrudApp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;

/**
 * @author Harideep Reddy Boothpur
 * @created 4/28/25 12:47 PM
 */

@Entity
public class Task {


    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String taskName;
    private String taskDescription;
    private String taskStatus;
    private String taskPriority;
    private LocalDate taskDueDate;
    private String taskAssignedTo;



    public enum TaskStatus {
        PENDING,
        IN_PROGRESS,
        FINISHED
    }
    public Long getId() {
        return id;
    }








    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(String taskPriority) {
        this.taskPriority = taskPriority;
    }

    public LocalDate getTaskDueDate() {
        return taskDueDate;
    }

    public String getTaskAssignedTo() {
        return taskAssignedTo;
    }

    public void setTaskAssignedTo(String taskAssignedTo) {
        this.taskAssignedTo = taskAssignedTo;
    }



}
