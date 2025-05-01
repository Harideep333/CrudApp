package com.EMS.CrudApp.Service;

import com.EMS.CrudApp.Repository.AttendanceRepository;
import com.EMS.CrudApp.Repository.UserRepository;
import com.EMS.CrudApp.entity.Attendance;
import com.EMS.CrudApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Harideep Reddy Boothpur
 * @created 4/29/25 9:45 PM
 */
@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    public void clockIn(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        User user = optionalUser.get();
        Optional<Attendance> existingAttendance = attendanceRepository.findTopByUserAndClockOutTimeIsNullOrderByClockInTimeDesc(user);
        if (existingAttendance.isPresent()) {
            throw new IllegalStateException("User is already clocked in.");
        }
        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setClockInTime(LocalDateTime.now()); // Setting thr  clock-in time
        attendance.setClockedIn(true);  // Mark a user as clocked in
        attendance.setClockedOut(false); // Ensure clocked out is false when ckickedin -in
        attendance.setUsername(username);  // Set the username
        System.out.println("saving attendance" + attendance);
        attendanceRepository.save(attendance);
    }


    public void clockOut(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        User user = optionalUser.get();
        Optional<Attendance> optionalAttendance = attendanceRepository.findTopByUserAndClockOutTimeIsNullOrderByClockInTimeDesc(user);

        if (optionalAttendance.isPresent()) {
            Attendance attendance = optionalAttendance.get();
            attendance.setClockOutTime(LocalDateTime.now()); // Set clock-out time
            attendance.calculateWorkDuration(); // Calculate the work duration based on clock-in and clock-out time
            attendance.setClockedIn(false);  // Mark user as clocked out
            attendance.setClockedOut(true); // Ensure clocked out is set to true on clock-out
            attendanceRepository.save(attendance);
        } else {
            throw new IllegalStateException("No active clock-in found for user.");
        }
    }


    public List<Attendance> getAttendanceForUser(String username) {
        return attendanceRepository.findByUsernameOrderByClockedInDesc(username);
    }

    public boolean isClockedIn(String username) {

        Attendance lastAttendance = attendanceRepository.findTopByUserAndClockOutTimeIsNullOrderByClockInTimeDesc(userService.findByUsername(username)).orElse(null);
        return lastAttendance != null && lastAttendance.getClockOutTime() == null;

    }

    public List<Attendance> getAllAttendance(){
        return attendanceRepository.findAll();
    }
}