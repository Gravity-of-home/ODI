package com.homegravity.Odi.domain.member.dto.request;

import com.homegravity.Odi.domain.member.dto.MemberBrixDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Schema(description = "동승자 합승 종료 후 동승자 평가")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberBrixListRequestDTO {

    @Schema(description = "파티 아이디")
    private Long partyId;

    @Schema(description = "파티 동승자 평가 리스트")
    private List<MemberBrixDTO> memberBrixDTOList;
}
