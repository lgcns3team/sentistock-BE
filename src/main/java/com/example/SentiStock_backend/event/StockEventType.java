package com.example.SentiStock_backend.event;

public enum StockEventType {
    PRICE_CHANGED,        // 주가 변화
    SENTIMENT_CHANGED,    // 감정 점수 변화
    VOLUME_SPIKE          // 거래량 급증
}
