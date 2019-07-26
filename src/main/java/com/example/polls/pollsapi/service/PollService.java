/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.polls.pollsapi.service;

import com.example.polls.pollsapi.exception.BadRequestException;
import com.example.polls.pollsapi.exception.ResourceNotFoundException;
import com.example.polls.pollsapi.model.*;
import com.example.polls.pollsapi.payload.PagedResponse;
import com.example.polls.pollsapi.payload.PollRequest;
import com.example.polls.pollsapi.payload.PollResponse;
import com.example.polls.pollsapi.payload.VoteRequest;
import com.example.polls.pollsapi.repository.PollRepository;
import com.example.polls.pollsapi.repository.VoteRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.example.polls.pollsapi.repository.UserRepository;
import com.example.polls.pollsapi.security.UserPrincipal;
import com.example.polls.pollsapi.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 *
 * @author nuna
 */
@Service
public class PollService {
    
    
    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoteRepository voteRepository;

    private static final Logger logger = LoggerFactory.getLogger(PollService.class);
    
    public Poll createPoll(PollRequest pollRequest) {
        Poll poll = new Poll();
        poll.setQuestion(pollRequest.getQuestion());
        
        pollRequest.getChoices().forEach(choiceRequest -> {
            poll.addChoice(new Choice(choiceRequest.getText()));
        });
        
        Instant now = Instant.now();
        Instant expirationDateTime = now.plus(Duration.ofDays(pollRequest.getPollLength().getDays()))
                .plus(Duration.ofHours(pollRequest.getPollLength().getHours()));
        
        poll.setExpirationDateTime(expirationDateTime);
        
        return pollRepository.save(poll);
    }

//    public PagedResponse<PollResponse> getAllPolls(UserPrincipal currentUser, int page, int size) {
//        validatePageNumberAndSize(page, size);
//
//        // Retrieve polls
//        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC,"createdAt");
//        Page<Poll> polls = pollRepository.findAll(pageable);
//
//        // Map Polls to PollResponses containing vote counts and poll creator details
//        List<Long> pollIds = polls.map(Poll::getId).getContent();
//        Map<Long, Long> choiceVoteCountMap = getChoiceVoteCountMap(pollIds);
//
//    }

//    private Map<Long, Long> getChoiceVoteCountMap(List<Long> pollIds) {
//        // Retrieve Vote Counts of every Choice belonging to the given pollIds
//        List<ChoiceVoteCount> votes = vote
//    }

    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.")
        }
        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than "+ AppConstants.MAX_PAGE_SIZE);
        }
    }

    public PollResponse castVoteAndGetUpdatePoll(Long pollId, VoteRequest voteRequest, UserPrincipal currentUser) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new ResourceNotFoundException("Poll", "id", pollId));

        if(poll.getExpirationDateTime().isBefore(Instant.now())) {
            throw new BadRequestException(("Sorry! This Poll has already expired");
        }

        User user = userRepository.getOne(currentUser.getId());

        Choice selectedChoice = poll.getChoices().stream()
                .filter(choice -> choice.getId().equals(voteRequest.getChoiceId()))
                .findFirst()
                .orElseThrow(()->new ResourceNotFoundException("Choice", "id", voteRequest.getChoiceId()));

        Vote vote = new Vote();
        vote.setPoll(poll);
        vote.setUser(user);
        vote.setChoice(selectedChoice);

        try{
            vote = voteRepository.save(vote);
        }catch (DataIntegrityViolationException ex) {
            logger.info("User {} has already voted in Poll {}", currentUser.getId(), pollId);
            throw new BadRequestException("Sorry! You have already cast your vote in this poll");
        }


    }
}
