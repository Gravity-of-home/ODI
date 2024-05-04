package com.homegravity.Odi.domain.party.dto.response;

import com.homegravity.Odi.domain.party.entity.PartySettlement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "정산 응답 DTO")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartySettlementResponseDto {

    @Schema(description = "파티 아이디")
    private Long partyId;

    @Schema(description = "영수증 사진")
    private String image;

    @Schema(description = "예상 택시비(선불)")
    private Integer prepaidCost;

    @Schema(description = "최종 택시비(정산)")
    private Integer cost;

    @Schema(description = "정산 담당자")
    private Long memberId;

    @Builder
    private PartySettlementResponseDto(Long partyId, String image, Integer prepaidCost, Integer cost, Long memberId) {
        this.partyId = partyId;
        this.image = image;
        this.prepaidCost = prepaidCost;
        this.cost = cost;
        this.memberId = memberId;
    }

    public static PartySettlementResponseDto from(PartySettlement partySettlement) {

        return builder()
                .partyId(partySettlement.getId())
                .image(partySettlement.getImage())
                .prepaidCost(partySettlement.getPrepaidCost())
                .cost(partySettlement.getCost())
                .memberId(partySettlement.getMemberId())
                .build();
    }
}
