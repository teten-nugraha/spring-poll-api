package com.example.polls.pollsapi.controller;

import com.example.polls.pollsapi.exception.ResourceNotFoundException;
import com.example.polls.pollsapi.model.User;
import com.example.polls.pollsapi.payload.UserProfile;
import com.example.polls.pollsapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value="username") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        
        UserProfile profile = new UserProfile(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getCreatedAt()
        );
        
        return profile;
    }

}
