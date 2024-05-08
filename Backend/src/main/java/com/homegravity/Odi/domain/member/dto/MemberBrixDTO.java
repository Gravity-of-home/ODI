package com.homegravity.Odi.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberBrixDTO {

    @Schema(description = "멤버 아이디(리뷰 받는 사람)")
    private Long reviewee_id;

    @Schema(description = "친절 매너 점수")
    private Integer kindScore;

    @Schema(description = "시간 약속 점수")
    private Integer promiseScore;

    @Schema(description = "빠른 응답 점수")
    private Integer fastChatScore;
}
