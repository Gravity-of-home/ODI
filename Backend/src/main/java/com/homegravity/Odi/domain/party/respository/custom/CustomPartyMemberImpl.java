package com.homegravity.Odi.domain.party.respository.custom;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.party.dto.PartyMemberDTO;
import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.entity.QPartyMember;
import com.homegravity.Odi.domain.party.entity.RoleType;
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
                .where(qPartyMember.role.eq(RoleType.GUEST)
                        .and(qPartyMember.party.eq(party))
                        .and(qPartyMember.deletedAt.isNull()))
                .fetchOne();
    }

    @Override
    public RoleType findParticipantRole(Member member) {
        QPartyMember qPartyMember = QPartyMember.partyMember;
        return jpaQueryFactory.selectFrom(qPartyMember)
                .where(qPartyMember.member.eq(member)
                        .and(qPartyMember.deletedAt.isNull()))
                .fetchOne().getRole();
    }

    @Override
    public List<PartyMemberDTO> findAllPartyGuests(Party party) {
        QPartyMember qPartyMember = QPartyMember.partyMember;
        return jpaQueryFactory.selectFrom(qPartyMember)
                .where(qPartyMember.role.eq(RoleType.GUEST)
                        .and(qPartyMember.deletedAt.isNull()))
                .fetch().stream().map(PartyMemberDTO::from).toList();
    }

    @Override
    public List<PartyMemberDTO> findAllPartyParticipants(Party party) {
        QPartyMember qPartyMember = QPartyMember.partyMember;
        return jpaQueryFactory.select(qPartyMember)
                .from(qPartyMember)
                .where(qPartyMember.party.eq(party)
                        .and(qPartyMember.deletedAt.isNull()))
                .fetch().stream().map(PartyMemberDTO::from).toList();
    }
}
