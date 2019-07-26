/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.polls.pollsapi.controller;

import com.example.polls.pollsapi.model.Poll;
import com.example.polls.pollsapi.payload.*;
import com.example.polls.pollsapi.security.CurrentUser;
import com.example.polls.pollsapi.security.UserPrincipal;
import com.example.polls.pollsapi.service.PollService;
import java.net.URI;
import javax.naming.ldap.PagedResultsResponseControl;
import javax.validation.Valid;

import com.example.polls.pollsapi.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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

    private static final Logger logger = LoggerFactory.getLogger(PollController.class);

//    @GetMapping
//    public PagedResponse<PollResponse> getPolls(@CurrentUser UserPrincipal currentUser,
//                                                @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page),
//                                                @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
//        return pollService.getAllPolls(currentUser, page, size);
//    }
    
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

    @PostMapping("/{pollId}/votes")
    @PreAuthorize("hasRole('USER')")
    public PollResponse castVote(@CurrentUser UserPrincipal currentUser,
                                 @PathVariable Long pollId,
                                 @Valid @RequestBody VoteRequest voteRequest) {
        return pollService.castVoteAndGetUpdatePoll(pollId, voteRequest, currentUser);
    }
    
}
