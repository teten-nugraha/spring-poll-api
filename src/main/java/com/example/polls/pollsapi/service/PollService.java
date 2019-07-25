/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.polls.pollsapi.service;

import com.example.polls.pollsapi.model.Choice;
import com.example.polls.pollsapi.model.Poll;
import com.example.polls.pollsapi.payload.PollRequest;
import com.example.polls.pollsapi.repository.PollRepository;
import java.time.Duration;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author nuna
 */
@Service
public class PollService {
    
    
    @Autowired
    private PollRepository pollRepository;
    
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
    
}
