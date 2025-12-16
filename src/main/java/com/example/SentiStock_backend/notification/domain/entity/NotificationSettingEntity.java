package com.example.SentiStock_backend.notification.domain.entity;

import com.example.SentiStock_backend.user.domain.entity.UserEntity;
import jakarta.persistence.*;   
import lombok.*;    


@Entity
@Table(
    name = "notification_settings",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "user_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationSettingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "profit_change", nullable = false)
    private Double profitChange;

    @Column(name = "senti_change", nullable = false)
    private Double sentiChange;
}
