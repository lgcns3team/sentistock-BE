package com.example.SentiStock_backend.sentiment.domain.entity;


import com.example.SentiStock_backend.news.domain.entity.NewsEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Sentiments") // 실제 테이블명 맞게 수정
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SentimentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prob_pos", nullable = false)
    private Double probPos;  //   긍정 확률
    @Column(name = "prob_neg", nullable = false)
    private Double probNeg;  //   부정 확률
    @Column(name = "prob_neu", nullable = false)
    private Double probNeu;  //   중립 확률 
    @Column(name = "label", nullable = false)  
    private String label; //   감정 라벨
    @Column(name = "score", nullable = false)
    private Double score; //   감정 점수
    @Column(name = "date", nullable = false)
    private String date;  //   감정 분석 날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private NewsEntity news;  //   뉴스 ID
}