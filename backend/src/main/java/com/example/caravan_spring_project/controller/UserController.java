package com.example.caravan_spring_project.controller;

import com.example.caravan_spring_project.CaravanSpringProjectApplication;
import com.example.caravan_spring_project.dto.UserDTO;
import com.example.caravan_spring_project.model.User;
import com.example.caravan_spring_project.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(CaravanSpringProjectApplication.class);

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable("id") Long id) {
        log.info("CHANGED Request for a user incoming with id:" + id);

        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }

    @GetMapping
    public List<UserDTO> findAll() {
        log.info("Request for all users");
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public UserDTO createUser(@RequestBody User user) {
        log.info("Creating new user: {}", user.getEmail());
        return convertToDTO(userRepository.save(user));
    }


    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        log.info("Updating user with id: {}", id);
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.getName());
                    user.setEmail(updatedUser.getEmail());
                    user.setPassword(updatedUser.getPassword());
                    return convertToDTO(userRepository.save(user));
                })
                .orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("Deleting user with id: {}", id);
        userRepository.deleteById(id);
    }


    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
