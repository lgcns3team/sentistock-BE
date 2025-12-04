package com.example.SentiStock_backend.sentiment.service;

import com.example.SentiStock_backend.sentiment.domain.dto.SentimentResponseDTO;

import java.util.List;

public interface SentimentService {

    // 종목 감정 점수 평균 산출
    Double getAverageSentimentScore(String companyId);

    // 감정 히스토리 조회
    List<SentimentResponseDTO> getSentimentHistory(String companyId);

    // 최근 기사 3개의 감정 점수 조회
    List<SentimentResponseDTO> getRecentNewsSentiments(String companyId);

    // 감정 점수 저장
    void saveSentiment(SentimentResponseDTO dto);

    // 감정 변화에 따른 알림 판단 로직
    boolean checkAlertCondition(String companyId);
}
