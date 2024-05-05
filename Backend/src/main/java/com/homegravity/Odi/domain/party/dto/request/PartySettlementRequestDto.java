package com.homegravity.Odi.domain.party.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "결제 요청 DTO")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartySettlementRequestDto {

    @Schema(description = "영수증 이미지")
    @NotNull(message = "영수증 이미지는 필수입니다.")
    private String image;

    @Schema(description = "cost")
    @NotNull(message = "정산 금액 설정은 필수입니다.")
    private Integer cost;

}
