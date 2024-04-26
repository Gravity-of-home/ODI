package com.homegravity.Odi.domain.party.dto;

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
    private Boolean isPaid;

    @Builder
    private PartyMemberDTO(Long id, RoleType role, Boolean isPaid) {
        this.id = id;
        this.role = role;
        this.isPaid = isPaid;
    }

    public static PartyMemberDTO from(PartyMember partyMember) {
        return PartyMemberDTO.builder()
                .id(partyMember.getId())
                .role(partyMember.getRole())
                .isPaid(partyMember.getIsPaid())
                .build();
    }

}
