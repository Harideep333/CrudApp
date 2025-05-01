package com.EMS.CrudApp.Repository;

import com.EMS.CrudApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Harideep Reddy Boothpur
 * @created 4/27/25 11:21 PM
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);


    List<User> findByIdNot(Long id);
}
