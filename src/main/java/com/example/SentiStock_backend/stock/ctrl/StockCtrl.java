package com.example.SentiStock_backend.stock.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SentiStock_backend.stock.service.StockService;

@RestController
@RequestMapping("/api/v1/stock")
public class StockCtrl {
    @Autowired
    private StockService stockService;
}
