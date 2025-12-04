package com.example.SentiStock_backend.sentiment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SentiStock_backend.sentiment.domain.entity.SentimentEntity;

@Repository
public interface SentimentRepository extends JpaRepository<SentimentEntity, Long> {

    
} 
    

