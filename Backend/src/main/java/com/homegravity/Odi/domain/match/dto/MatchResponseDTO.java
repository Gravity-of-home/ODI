package com.homegravity.Odi.domain.match.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchResponseDTO {

    @Schema(description = "결과 타입")
    private ResultType type;

    @Schema(description = "멤버1 id")
    private Long memberId1;

    @Schema(description = "멤버2 id")
    private Long memberId2;

    @Schema(description = "생성된 파티 id")
    private Long partyId;

    @Schema(description = "요청정보")
    private MatchRequestDTO request;

    @Builder
    private MatchResponseDTO(ResultType type, Long memberId1, Long memberId2, Long partyId, MatchRequestDTO request) {
        this.type = type;
        this.memberId1 = memberId1;
        this.memberId2 = memberId2;
        this.partyId = partyId;
        this.request = request;
    }

    public static MatchResponseDTO of(ResultType type, Long memberId1, Long memberId2, Long partyId, MatchRequestDTO request) {
        return builder()
                .type(type)
                .memberId1(memberId1)
                .memberId2(memberId2)
                .partyId(partyId)
                .request(request)
                .build();
    }
}
