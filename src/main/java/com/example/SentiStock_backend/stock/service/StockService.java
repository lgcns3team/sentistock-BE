package com.example.SentiStock_backend.stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SentiStock_backend.stock.repository.StockRepository;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;
}
