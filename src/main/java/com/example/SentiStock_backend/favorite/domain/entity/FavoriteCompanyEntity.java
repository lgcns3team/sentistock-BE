package com.example.SentiStock_backend.favorite.domain.entity;

import com.example.SentiStock_backend.company.domain.entity.CompanyEntity;
import com.example.SentiStock_backend.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(
    name = "Favorites_companies",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "company_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(FavoriteCompanyId.class)
public class FavoriteCompanyEntity {


    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "company_id", nullable = false, length = 10)
    private String companyId;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private CompanyEntity company;


    public static FavoriteCompanyEntity of(UserEntity user, CompanyEntity company) {
        return FavoriteCompanyEntity.builder()
                .userId(user.getId())       
                .companyId(company.getId())  
                .user(user)
                .company(company)
                .build();
    }
}
