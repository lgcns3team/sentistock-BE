package com.example.SentiStock_backend.sentiment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SentiStock_backend.sentiment.repository.SentimentRepository;

@Service
public class SentimentService {
    @Autowired
    private SentimentRepository sentimentRepository;
}
