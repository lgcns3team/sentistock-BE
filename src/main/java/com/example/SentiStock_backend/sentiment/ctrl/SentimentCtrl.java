package com.example.SentiStock_backend.sentiment.ctrl;

import com.example.SentiStock_backend.sentiment.domain.dto.SentimentRatioResponseDTO;
import com.example.SentiStock_backend.sentiment.domain.dto.SentimentResponseDTO;
import com.example.SentiStock_backend.sentiment.domain.dto.StocksScoreResponseDTO;
import com.example.SentiStock_backend.sentiment.service.SentimentService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sentiment")
public class SentimentCtrl {

    private final SentimentService sentimentService;

    /** 종목 평균 감정 점수 조회 */
    @Operation(summary = "종목 평균 감정 점수 조회")
    @GetMapping("/score/{companyId}")
    public Double getCompanySentimentScore(@PathVariable String companyId) {
        return sentimentService.getCompanySentimentScore(companyId);
    }

    /** 기사 최신 감정 점수 3개 조회 */
    @Operation(summary = "최신 뉴스 감정 점수 3개 조회")
    @GetMapping("/recent/{companyId}")
    public List<SentimentResponseDTO> getRecentSentiments(@PathVariable String companyId) {
        return sentimentService.getRecent3Sentiments(companyId);
    }

    /** 종목 감정 점수 7개 조회 (히스토리) */
    @Operation(summary = "종목 감정 점수 히스토리 조회 (최근 7개)")
    @GetMapping("/history/{companyId}")
    public List<StocksScoreResponseDTO> getSentimentHistory(@PathVariable String companyId) {
        return sentimentService.getSentimentHistory(companyId);
    }

    /** 종목 감정 점수 저장(수동 실행용) */
    @Operation(summary = "감정 점수 저장 (수동 실행)")
    @PutMapping("/save/{companyId}")
    public void saveScore(@PathVariable String companyId) {
        sentimentService.saveCompanySentimentScore(companyId);
    }

    /** 종목 감정 비율 조회 */
    @Operation(summary = "종목 감정 비율 조회")
    @GetMapping("/ratio/{companyId}")
    public ResponseEntity<SentimentRatioResponseDTO> getSentimentRatio(
            @PathVariable String companyId
    ) {
        return ResponseEntity.ok(sentimentService.getSentimentRatio(companyId));
    }
}
