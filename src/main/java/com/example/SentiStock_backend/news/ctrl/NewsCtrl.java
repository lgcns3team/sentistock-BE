package com.example.SentiStock_backend.news.ctrl;

import com.example.SentiStock_backend.news.domain.dto.NewsSentimentDTO;
import com.example.SentiStock_backend.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsCtrl {

    private final NewsService newsService;

    /** 최신 뉴스 3건 + 감정 점수 조회 */
    @GetMapping("/recent-score/{companyId}")
    public List<NewsSentimentDTO> getRecentNewsSentiment(@PathVariable String companyId) {
        return newsService.getRecentNewsWithSentiment(companyId);
    }
}
