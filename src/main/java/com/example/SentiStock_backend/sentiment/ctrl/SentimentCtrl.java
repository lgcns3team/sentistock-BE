package com.example.SentiStock_backend.sentiment.ctrl;

import com.example.SentiStock_backend.sentiment.domain.dto.SentimentResponseDTO;
import com.example.SentiStock_backend.sentiment.service.SentimentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sentiment")
public class SentimentCtrl {

    private final SentimentService sentimentService;

    /** 종목 평균 감정 점수 조회 */
    @GetMapping("/score/{companyId}")
    public Double getCompanySentimentScore(@PathVariable String companyId) {
        return sentimentService.getCompanySentimentScore(companyId);
    }

    /** 종목 최신 감정 점수 3개 조회 */
    @GetMapping("/recent/{companyId}")
    public List<SentimentResponseDTO> getRecentSentiments(@PathVariable String companyId) {
        return sentimentService.getRecent3Sentiments(companyId);
    }

    /** 감정 히스토리(최근 7일) 조회 */
    @GetMapping("/history/{companyId}")
    public List<SentimentResponseDTO> getSentimentHistory(@PathVariable String companyId) {
        return sentimentService.getSentimentHistory(companyId);
    }
}
