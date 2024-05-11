package com.homegravity.Odi.domain.party.dto;

import com.homegravity.Odi.domain.party.entity.PartyMember;
import com.homegravity.Odi.domain.party.entity.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "파티 참여 맴버 상세 정보 DTO")
@Getter
@Setter
public class PartyMemberDTO {

    @Schema(description = "파티맴버 id")
    private Long id;

    @Schema(description = "파티 역할(파티장, 참여자)")
    private RoleType role;

    @Schema(description = "닉네임")
    private String nickname;

    @Schema(description = "성별")
    private String gender;

    @Schema(description = "나이대")
    private String ageGroup;

    @Schema(description = "프로필 사진")
    private String profileImage;

    @Schema(description = "택시비 지불 유무")
    private Boolean isPaid;

    @Schema(description = "매너 당도")
    private Double brix;

    @Builder
    private PartyMemberDTO(Long id, RoleType role, String nickname, String gender, String ageGroup, String profileImage, Boolean isPaid, Double brix) {
        this.id = id;
        this.role = role;
        this.nickname = nickname;
        this.gender = gender;
        this.ageGroup = ageGroup;
        this.profileImage = profileImage;
        this.isPaid = isPaid;
        this.brix = brix;
    }

    public static PartyMemberDTO from(PartyMember partyMember) {

        return PartyMemberDTO.builder()
                .id(partyMember.getMember().getId())
                .role(partyMember.getRole())
                .nickname(partyMember.getMember().getNickname())
                .gender(partyMember.getMember().getGender())
                .ageGroup(partyMember.getMember().getAgeGroup())
                .profileImage(partyMember.getMember().getImage())
                .isPaid(partyMember.getIsPaid())
                .brix(partyMember.getMember().getBrix())
                .build();

    }

}
