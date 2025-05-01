package com.EMS.CrudApp.Repository;

import com.EMS.CrudApp.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Harideep Reddy Boothpur
 * @created 4/28/25 12:50 PM
 */

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


    List<Task> findByTaskAssignedTo(String username);

    List<Task> findAll();


}
