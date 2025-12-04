package com.example.SentiStock_backend.sentiment.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SentiStock_backend.sentiment.service.SentimentService;

@RestController
@RequestMapping("api/v1/sentiment")
public class SentimentCtrl {

    @Autowired
    private SentimentService sentimentService;
    
}
