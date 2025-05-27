package com.example.caravan_spring_project.controller;

import com.example.caravan_spring_project.CaravanSpringProjectApplication;
import com.example.caravan_spring_project.model.User;
import com.example.caravan_spring_project.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(CaravanSpringProjectApplication.class);

    UserRepository userRepository;

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
}
