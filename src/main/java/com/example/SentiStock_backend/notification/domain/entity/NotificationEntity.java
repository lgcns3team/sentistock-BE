package com.example.SentiStock_backend.notification.domain.entity;

import java.time.LocalDateTime;

import com.example.SentiStock_backend.user.domain.entity.UserEntity;
import com.example.SentiStock_backend.company.domain.entity.CompanyEntity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "Notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content", nullable = false, length = 50)
    private String content;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "is_check", nullable = false)
    private boolean isCheck;

    @Column(name = "profit_change", nullable = false)
    private Double profitChange;

    @Column(name = "senti_change", nullable = false)
    private Double sentiChange;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyEntity company;

}
