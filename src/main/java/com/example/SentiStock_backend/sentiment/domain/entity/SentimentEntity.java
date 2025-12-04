package com.example.sentiment.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(nullable = false)
    private Long probPos;  //   긍정 확률
    private Long probNeg;  //   부정 확률
    private Long probNeu;  //   중립 확률   
    private String label; //   감정 라벨
    private Long score; //   감정 점수
    private String date;  //   감정 분석 날짜

    @Column(length = 10)
    private String newsId;  //   뉴스 ID
}