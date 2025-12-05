package com.example.SentiStock_backend.news.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 뉴스 ID

    @Column(nullable = false, length = 100)
    private String title; // 뉴스 제목

    @Column(nullable = false)
    private LocalDateTime date; // 뉴스 날짜 (Datetime)

    @Column(nullable = false, columnDefinition = "TEXT")
    private String fullText; // 뉴스 본문

    @Column(nullable = false, length = 500)
    private String url; // 뉴스 URL

    @Column(length = 50)
    private String summaryText; // 뉴스 요약문 (NULL 허용)

    @Column(nullable = false)
    private Long companyId; // 종목 코드 (FK 역할)
}
