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
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomPartyMemberImpl implements CustomPartyMember {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public int countAllPartyGuests(Party party) {
        QPartyMember qPartyMember = QPartyMember.partyMember;
        Optional<Long> count = Optional.ofNullable(jpaQueryFactory.select(qPartyMember.count())
                .from(qPartyMember)
                .where(qPartyMember.role.eq(RoleType.REQUESTER)
                        .and(qPartyMember.party.eq(party))
                        .and(qPartyMember.deletedAt.isNull()))
                .fetchOne());

        return count.map(Long::intValue).orElse(0);
    }

    @Override
    public RoleType findParticipantRole(Party party, Member member) {
        QPartyMember qPartyMember = QPartyMember.partyMember;

        Optional<PartyMember> partyMember = Optional.ofNullable(jpaQueryFactory.selectFrom(qPartyMember)
                .where(qPartyMember.member.eq(member)
                        .and(qPartyMember.party.eq(party))
                        .and(qPartyMember.deletedAt.isNull()))
                .fetchOne());

        return partyMember.map(PartyMember::getRole).orElse(null);
    }

    @Override
    public List<PartyMemberDTO> findAllPartyMember(Party party, RoleType role) {
        QPartyMember qPartyMember = QPartyMember.partyMember;

        BooleanBuilder builder = new BooleanBuilder();

        if (role == RoleType.REQUESTER) { // 신청자 목록
            builder.and(qPartyMember.role.eq(RoleType.REQUESTER));
        }
        // 파티장, 파티원 목록
        else if (role == RoleType.PARTICIPANT) {
            builder.and(qPartyMember.role.eq(RoleType.ORGANIZER))
                    .or(qPartyMember.role.eq(RoleType.PARTICIPANT));
        }


        return jpaQueryFactory.selectFrom(qPartyMember)
                .where(qPartyMember.party.eq(party)
                        .and(builder)
                        .and(qPartyMember.deletedAt.isNull()))
                .fetch().stream().map(pm -> PartyMemberDTO.of(pm, pm.getMember())).toList();
    }

    @Override
    public Optional<PartyMember> findOrganizer(Party party) {
        QPartyMember qPartyMember = QPartyMember.partyMember;

        return Optional.ofNullable(jpaQueryFactory.selectFrom(qPartyMember)
                .where(qPartyMember.party.eq(party)
                        .and(qPartyMember.role.eq(RoleType.ORGANIZER))
                        .and(qPartyMember.deletedAt.isNull()))
                .fetchOne());

    }

    // 해당 파티에 신청한 사용자가 존재하는지 boolean 확인
    @Override
    public boolean existPartyMember(Party party, Member member) {
        QPartyMember qPartyMember = QPartyMember.partyMember;

        return jpaQueryFactory.selectFrom(qPartyMember)
                .where(qPartyMember.party.eq(party)
                        .and(qPartyMember.member.eq(member))
                        .and(qPartyMember.deletedAt.isNull()))
                .fetchFirst() != null;
    }


    //참여자 혹은 신청자인 경우일 때
    @Override
    public Optional<PartyMember> findPartyPartiAndReqByMember(Party party, Member member) {
        QPartyMember qPartyMember = QPartyMember.partyMember;

        return Optional.ofNullable(jpaQueryFactory.selectFrom(qPartyMember)
                .where(qPartyMember.party.eq(party)
                        .and(qPartyMember.member.eq(member))
                        .and(qPartyMember.role.eq(RoleType.PARTICIPANT)
                                .or(qPartyMember.role.eq(RoleType.REQUESTER)))
                        .and(qPartyMember.deletedAt.isNull()))
                .fetchOne());
    }

    @Override
    public List<PartyMember> findAllPartyMember(Party party) {
        QPartyMember qPartyMember = QPartyMember.partyMember;

        return jpaQueryFactory.selectFrom(qPartyMember)
                .where(qPartyMember.role.ne(RoleType.REQUESTER)
                        .and(qPartyMember.party.eq(party))
                        .and(qPartyMember.deletedAt.isNull()))
                .fetch();
    }

    @Override
    public List<PartyMember> findAllPartyMemberAndRequester(Party party) {
        QPartyMember qPartyMember = QPartyMember.partyMember;

        return jpaQueryFactory.selectFrom(qPartyMember)
                .where(qPartyMember.party.eq(party)
                        .and(qPartyMember.deletedAt.isNull()))
                .fetch();
    }

    @Override
    public Optional<PartyMember> findByPartyAndMember(Party party, Member member) {
        QPartyMember qPartyMember = QPartyMember.partyMember;

        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(qPartyMember)
                        .where(qPartyMember.party.eq(party)
                                .and(qPartyMember.member.eq(member))
                                .and(qPartyMember.role.ne(RoleType.REQUESTER))
                                .and(qPartyMember.deletedAt.isNull()))
                        .fetchOne()
        );
    }


}
