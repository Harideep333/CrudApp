package com.EMS.CrudApp.Repository;

import com.EMS.CrudApp.entity.Attendance;
import com.EMS.CrudApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Harideep Reddy Boothpur
 * @created 4/29/25 9:40 PM
 */


@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    <user> Optional<Attendance> findTopByUserAndClockOutTimeIsNullOrderByClockInTimeDesc(User user);


    List<Attendance> findByUsernameOrderByClockedInDesc(String username);

}