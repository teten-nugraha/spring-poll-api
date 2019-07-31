package com.example.polls.pollsapi.payload;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

public class PollResponse {



    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long selectedChoice;

    public Long getSelectedChoice() {
        return selectedChoice;
    }

    public void setSelectedChoice(Long selectedChoice) {
        this.selectedChoice = selectedChoice;
    }


}
