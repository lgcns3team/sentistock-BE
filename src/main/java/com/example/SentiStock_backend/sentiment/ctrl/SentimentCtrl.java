package com.example.SentiStock_backend.sentiment.ctrl;

import com.example.SentiStock_backend.sentiment.domain.dto.SentimentRatioResponseDto;
import com.example.SentiStock_backend.sentiment.domain.dto.SentimentResponseDto;
import com.example.SentiStock_backend.sentiment.domain.dto.StocksScoreResponseDto;
import com.example.SentiStock_backend.sentiment.service.SentimentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sentiment")
@Tag(name = "Sentiment API", description = "종목 감정 점수 조회 및 관리 관련 API")
public class SentimentCtrl {

    private final SentimentService sentimentService;

    /** 종목 평균 감정 점수 조회 */
    @Operation(summary = "종목 평균 감정 점수 조회", description = "지정된 회사 종목의 평균 감정 점수를 조회합니다.")
    @GetMapping("/score/{companyId}")
    public Double getCompanySentimentScore(
            @Parameter(description = "회사 종목 코드", example = "005930") @PathVariable String companyId) {
        return sentimentService.getCompanySentimentScore(companyId);
    }

    /** 기사 최신 감정 점수 3개 조회 */
    @Operation(summary = "최신 뉴스 감정 점수 3개 조회", description = "지정된 회사 종목에 대한 최신 뉴스 3건의 감정 점수를 조회합니다.")
    @GetMapping("/recent/{companyId}")
    public List<SentimentResponseDto> getRecentSentiments(
            @Parameter(description = "회사 종목 코드", example = "005930") @PathVariable String companyId) {
        return sentimentService.getRecent3Sentiments(companyId);
    }

    /** 종목 감정 점수 7개 조회 (히스토리) */
    @Operation(summary = "종목 감정 점수 히스토리 조회 (최근 28개)", description = "지정된 회사 종목에 대한 최근 7개의 감정 점수 히스토리를 조회합니다.")
    @GetMapping("/history/{companyId}")
    public List<StocksScoreResponseDto> getSentimentHistory(
            @Parameter(description = "회사 종목 코드", example = "005930") @PathVariable String companyId) {
        return sentimentService.getSentimentHistory(companyId);
    }

    /** 종목 감정 점수 저장(수동 실행용) */
    @Operation(summary = "감정 점수 저장 (수동 실행)", description = "지정된 회사 종목의 감정 점수를 저장합니다. (수동 실행용)")
    @PutMapping("/save/{companyId}")
    public void saveScore(@Parameter(description = "회사 종목 코드", example = "005930") @PathVariable String companyId) {
        sentimentService.saveCompanySentimentScore(companyId);
    }

    /** 종목 감정 비율 조회 */
    @Operation(summary = "종목 감정 비율 조회", description = "지정된 회사 종목의 긍정, 부정, 중립 감정 비율을 조회합니다.")
    @GetMapping("/ratio/{companyId}")
    public ResponseEntity<SentimentRatioResponseDto> getSentimentRatio(
            @Parameter(description = "회사 종목 코드", example = "005930") @PathVariable String companyId) {
        return ResponseEntity.ok(sentimentService.getSentimentRatio(companyId));
    }
}
