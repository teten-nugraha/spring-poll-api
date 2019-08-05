package com.example.polls.pollsapi.controller;

import com.example.polls.pollsapi.exception.ResourceNotFoundException;
import com.example.polls.pollsapi.model.User;
import com.example.polls.pollsapi.payload.PagedResponse;
import com.example.polls.pollsapi.payload.PollResponse;
import com.example.polls.pollsapi.payload.UserProfile;
import com.example.polls.pollsapi.repository.UserRepository;
import com.example.polls.pollsapi.security.CurrentUser;
import com.example.polls.pollsapi.security.UserPrincipal;
import com.example.polls.pollsapi.service.PollService;
import com.example.polls.pollsapi.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollService pollService;
    
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

    @GetMapping("/users/{username}/polls")
    public PagedResponse<PollResponse> getPollsCreatedBy(@PathVariable(value = "username") String username,
                                                         @CurrentUser UserPrincipal currentUser,
                                                         @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                         @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return pollService.getPollsCreatedBy(username, currentUser, page, size);
    }

}
