package com.example.SentiStock_backend.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnboardingRequestDto {

    @Schema(description = "투자 성향 설문 점수 합계", example = "27")
    @NotNull(message = "설문 점수를 입력해주세요.")
    private Integer investorScore;

    @Schema(description = "관심 섹터 ID 리스트 (정확히 5개)", example = "[1, 2, 3, 4, 5]")
    @NotNull(message = "관심 섹터를 선택해주세요.")
    @Size(min = 5, max = 5, message = "관심 섹터는 정확히 5개 선택해야 합니다.")
    private List<Long> favoriteSectorIds;
}
