package com.EMS.CrudApp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author Harideep Reddy Boothpur
 * @created 4/29/25 9:37 PM
 */
@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @ManyToOne
    private User user;

    private LocalDateTime clockInTime;  // Clock-in time
    private LocalDateTime clockOutTime;  // Clock-out time
    private Duration workDuration;

    @Column(name = "is_clocked_in", nullable = false, columnDefinition = "boolean default false")
    private boolean isClockedIn = false;

    @Column(name = "is_clocked_out", nullable = false, columnDefinition = "boolean default false")
    private boolean isClockedOut = false;



    @Column(name = "clocked_in")
    private LocalDateTime clockedIn;
    @Column(name = "clocked_out")
    private LocalDateTime clockedOut;
    private String username;

    public Attendance(User user) {
        this.user = user;
    }

    public Attendance() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getClockInTime() {
        return clockInTime;
    }

    public void setClockInTime(LocalDateTime clockInTime) {
        this.clockInTime = clockInTime;
    }

    public LocalDateTime getClockOutTime() {
        return clockOutTime;
    }

    public void setClockOutTime(LocalDateTime clockOutTime) {
        this.clockOutTime = clockOutTime;
    }

    public Duration getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(Duration workDuration) {
        this.workDuration = workDuration;
    }

    public void calculateWorkDuration() {
        if (clockInTime != null && clockOutTime != null) {
            this.workDuration = Duration.between(clockInTime, clockOutTime);
        }
    }

    public boolean isClockedIn() {
        return isClockedIn;
    }

    public void setClockedIn(boolean clockedIn) {
        isClockedIn = clockedIn;
    }

    public boolean isClockedOut() {
        return isClockedOut;
    }

    public void setClockedOut(boolean clockedOut) {
        isClockedOut = clockedOut;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}