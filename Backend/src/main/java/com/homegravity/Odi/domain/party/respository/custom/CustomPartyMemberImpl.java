package com.homegravity.Odi.domain.party.respository.custom;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.party.dto.PartyMemberDTO;
import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.entity.PartyMember;
import com.homegravity.Odi.domain.party.entity.QPartyMember;
import com.homegravity.Odi.domain.party.entity.RoleType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomPartyMemberImpl implements CustomPartyMember {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Long countAllPartyGuests(Party party) {
        QPartyMember qPartyMember = QPartyMember.partyMember;
        return jpaQueryFactory.select(qPartyMember.count())
                .from(qPartyMember)
                .where(qPartyMember.role.eq(RoleType.REQUESTER)
                        .and(qPartyMember.party.eq(party))
                        .and(qPartyMember.deletedAt.isNull()))
                .fetchOne();
    }

    @Override
    public RoleType findParticipantRole(Party party, Member member) {
        QPartyMember qPartyMember = QPartyMember.partyMember;

        PartyMember partyMember =  jpaQueryFactory.selectFrom(qPartyMember)
                .where(qPartyMember.member.eq(member)
                        .and(qPartyMember.party.eq(party))
                        .and(qPartyMember.deletedAt.isNull()))
                .fetchOne();

        RoleType role = null;

        if(partyMember != null) {
            role = partyMember.getRole();
        }

        return role;
    }

    @Override
    public List<PartyMemberDTO> findAllPartyMember(Party party, RoleType role) {
        QPartyMember qPartyMember = QPartyMember.partyMember;

        BooleanBuilder builder = new BooleanBuilder();
        
        if(role == RoleType.REQUESTER) { // 신청자 목록은 파티장만
            builder.and(qPartyMember.role.eq(RoleType.REQUESTER));
        }
        // 파티장, 파티원 목록
        else if(role == RoleType.PARTICIPANT) {
            builder.and(qPartyMember.role.eq(RoleType.ORGANIZER))
                    .or(qPartyMember.role.eq(RoleType.PARTICIPANT));
        }


        return jpaQueryFactory.selectFrom(qPartyMember)
                .where(qPartyMember.party.eq(party)
                        .and(builder)
                        .and(qPartyMember.deletedAt.isNull()))
                .fetch().stream().map(pm -> PartyMemberDTO.of(pm, pm.getMember())).toList();
    }
}
