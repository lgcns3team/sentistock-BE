package com.example.SentiStock_backend.news.ctrl;

import com.example.SentiStock_backend.news.domain.dto.NewsSentimentDTO;
import com.example.SentiStock_backend.news.service.NewsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsCtrl {

    private final NewsService newsService;

    /** 최신 뉴스 3건 + 감정 점수 조회 */
    @Operation(summary = "최신 뉴스 3건 및 감정 점수 조회", description = "지정된 회사 종목에 대한 최신 뉴스 3건과 각 뉴스의 감정 점수를 조회합니다.")
    @GetMapping("/recent-score/{companyId}")
    public List<NewsSentimentDTO> getRecentNewsSentiment(
            @Parameter(description = "회사 종목 코드", example = "005930") @PathVariable String companyId) {
        return newsService.getRecentNewsWithSentiment(companyId);
    }
}
