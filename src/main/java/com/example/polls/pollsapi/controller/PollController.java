/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.polls.pollsapi.controller;

import com.example.polls.pollsapi.model.Poll;
import com.example.polls.pollsapi.payload.ApiResponse;
import com.example.polls.pollsapi.payload.PollRequest;
import com.example.polls.pollsapi.service.PollService;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author nuna
 */
@RestController
@RequestMapping("/api/polls")
public class PollController {
    
    @Autowired
    private PollService pollService;
    
    
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createPoll(@Valid @RequestBody PollRequest pollRequest){
        Poll poll = pollService.createPoll(pollRequest);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{pollId}")
                .buildAndExpand(poll.getId())
                .toUri();
        
        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Poll Created Successfully."));
    }
    
    
}
