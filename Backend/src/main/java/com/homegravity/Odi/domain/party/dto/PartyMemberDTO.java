package com.homegravity.Odi.domain.party.dto;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.party.entity.PartyMember;
import com.homegravity.Odi.domain.party.entity.RoleType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyMemberDTO {

    private Long id;
    private RoleType role;
    private String nickname;
    private String gender;
    private String ageGroup;
    private String profileImage;
    private Boolean isPaid;

    @Builder
    private PartyMemberDTO(Long id, RoleType role, String nickname, String gender, String ageGroup, String profileImage, Boolean isPaid) {
        this.id = id;
        this.role = role;
        this.nickname = nickname;
        this.gender = gender;
        this.ageGroup = ageGroup;
        this.profileImage = profileImage;
        this.isPaid = isPaid;
    }

    public static PartyMemberDTO of(PartyMember partyMember, Member member) {
        return PartyMemberDTO.builder()
                .id(partyMember.getId())
                .role(partyMember.getRole())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .ageGroup(member.getAgeGroup())
                .profileImage(member.getImage())
                .isPaid(partyMember.getIsPaid())
                .build();
    }

}
