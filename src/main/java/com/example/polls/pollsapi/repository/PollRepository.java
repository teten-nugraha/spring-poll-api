/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.polls.pollsapi.repository;

import com.example.polls.pollsapi.model.Poll;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nuna
 */
@Repository
public interface PollRepository extends JpaRepository<Poll, Long>{
    
    Optional<Poll> findById(Long pollId);
    
    Page<Poll> findByCreatedBy(Long userId, Pageable pageable);
    
    long countByCreatedBy(Long userId);
    
    List<Poll> findByIdIn(List<Long> pollIds);
    
    List<Poll> findByIdIn(List<Long> pollIds, Sort sort);
    
}
