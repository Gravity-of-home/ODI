package com.homegravity.Odi.domain.match.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchResponseDTO {

    @Schema(description = "멤버1 id")
    private Long memberId1;

    @Schema(description = "멤버2 id")
    private Long memberId2;

    @Schema(description = "생성된 파티 id")
    private Long partyId;

    @Builder
    private MatchResponseDTO(Long memberId1, Long memberId2, Long partyId) {
        this.memberId1 = memberId1;
        this.memberId2 = memberId2;
        this.partyId = partyId;
    }

    public static MatchResponseDTO of(Long memberId1, Long memberId2, Long partyId) {
        return builder()
                .memberId1(memberId1)
                .memberId2(memberId2)
                .partyId(partyId)
                .build();
    }

}
