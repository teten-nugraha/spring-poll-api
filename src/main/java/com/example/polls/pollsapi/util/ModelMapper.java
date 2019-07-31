package com.example.polls.pollsapi.util;

import com.example.polls.pollsapi.model.Poll;
import com.example.polls.pollsapi.model.User;
import com.example.polls.pollsapi.payload.ChoiceResponse;
import com.example.polls.pollsapi.payload.PollResponse;
import com.example.polls.pollsapi.payload.UserSummary;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelMapper {

    public static PollResponse mapPollToPollResponse(Long userVote) {
        PollResponse pollResponse = new PollResponse();

        if(userVote != null){
            pollResponse.setSelectedChoice(userVote);
        }

        return pollResponse;

    }

}
