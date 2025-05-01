package com.EMS.CrudApp.Service;

import com.EMS.CrudApp.Repository.UserRepository;
import com.EMS.CrudApp.UserDTO.UserDTO;
import com.EMS.CrudApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Harideep Reddy Boothpur
 * @created 4/27/25 11:23 PM
 */
@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return (List<User>) userRepository.findAll();
    }

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User user){
        Optional<User> existingUser = userRepository.findById(id);
        if(existingUser.isPresent()){
            User usertoUpdate = existingUser.get();
            usertoUpdate.setFirstName(user.getFirstName());
            usertoUpdate.setLastName(user.getLastName());
            usertoUpdate.setEmail(user.getEmail());
            usertoUpdate.setPassword(user.getPassword());
            usertoUpdate.setPhone(user.getPhone());
            usertoUpdate.setPassword(user.getPassword());
            return userRepository.save(usertoUpdate);
        }
        return null;
    }
    public List<User> saveAll(List<User> users) {
        return (List<User>) userRepository.saveAll(users);
    }

    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }




        public List<User> findByIdNot(Long id) {
            return userRepository.findByIdNot(id); // Assumes you have this method in your repository
        }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }


    public List<User> getAllEmployees() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains("ROLE_EMPLOYEE"))
                .collect(Collectors.toList());
    }

    public Long getUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public void save(User existingUser) {
        userRepository.save(existingUser);
    }

    public void saveManagerAddedEmployee(User user) {
        user.setRoles(Set.of("EMPLOYEE"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}



