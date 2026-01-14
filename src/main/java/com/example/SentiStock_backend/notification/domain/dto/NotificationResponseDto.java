package com.example.SentiStock_backend.notification.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDto {

    private Long id;
    private String content;
    private String type;
    private boolean isCheck;
    private String companyId;
    private LocalDateTime date;

}
