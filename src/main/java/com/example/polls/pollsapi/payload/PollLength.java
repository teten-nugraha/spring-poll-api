/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.polls.pollsapi.payload;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 *
 * @author nuna
 */
public class PollLength {
    
    @NotNull
    @Max(7)
    private Integer days;
    
    @NotNull
    @Max(23)
    private Integer hours;

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }
    
    
}
