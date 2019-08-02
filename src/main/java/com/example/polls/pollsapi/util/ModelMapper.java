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

    public static PollResponse mapPollToPollResponse(Poll poll,Map<Long, Long> choiceVotesMap,User creator, Long userVote) {
        PollResponse pollResponse = new PollResponse();
        pollResponse.setId(poll.getId());
        pollResponse.setQuestion(poll.getQuestion());
        pollResponse.setCreationDateTime(poll.getCreatedAt());
        pollResponse.setExpirationDateTime(poll.getExpirationDateTime());
        Instant now = Instant.now();
        pollResponse.setExpired(poll.getExpirationDateTime().isBefore(now));

        List<ChoiceResponse> choiceResponses = poll.getChoices().stream().map(choice -> {
           ChoiceResponse choiceResponse  =new ChoiceResponse();
           choiceResponse.setId(choice.getId());
           choiceResponse.setText(choice.getText());
           if(choiceVotesMap.containsKey(choice.getId())){
               choiceResponse.setVoteCount(choiceVotesMap.get(choice.getId()));
           }else{
               choiceResponse.setVoteCount(0);
           }

           return choiceResponse;
        }).collect(Collectors.toList());
        pollResponse.setChoices(choiceResponses);

        if(userVote != null){
            pollResponse.setSelectedChoice(userVote);
        }

        UserSummary userSummary = new UserSummary(
            creator.getId(),
                creator.getUsername(),
                creator.getName()
        );
        pollResponse.setCreatedBy(userSummary);


        long totalVotes = pollResponse.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum();

        pollResponse.setTotalVotes(totalVotes);

//        pollResponse
//                .getChoices()
//                .stream()
//                .mapToLong(ChoiceResponse::getVoteCount)
//                .forEach(System.out::println);

        return pollResponse;

    }

}
