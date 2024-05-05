package com.homegravity.Odi.domain.party.dto;

import com.homegravity.Odi.domain.party.entity.PartyMember;
import com.homegravity.Odi.domain.party.entity.RoleType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyMemberDTO {

    private Long member_id;
    private RoleType role;
    private String nickname;
    private String gender;
    private String ageGroup;
    private String profileImage;
    private Boolean isPaid;

    @Builder
    private PartyMemberDTO(Long member_id, RoleType role, String nickname, String gender, String ageGroup, String profileImage, Boolean isPaid) {
        this.member_id = member_id;
        this.role = role;
        this.nickname = nickname;
        this.gender = gender;
        this.ageGroup = ageGroup;
        this.profileImage = profileImage;
        this.isPaid = isPaid;
    }

    public static PartyMemberDTO from(PartyMember partyMember) {
        return PartyMemberDTO.builder()
                .member_id(partyMember.getMember().getId())
                .role(partyMember.getRole())
                .nickname(partyMember.getMember().getNickname())
                .gender(partyMember.getMember().getGender())
                .ageGroup(partyMember.getMember().getAgeGroup())
                .profileImage(partyMember.getMember().getImage())
                .isPaid(partyMember.getIsPaid())
                .build();
    }

}
