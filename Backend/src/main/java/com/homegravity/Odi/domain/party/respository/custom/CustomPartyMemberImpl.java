package com.homegravity.Odi.domain.party.respository.custom;

import com.homegravity.Odi.domain.member.dto.response.MemberPartyHistoryResponseDTO;
import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.party.dto.PartyMemberDTO;
import com.homegravity.Odi.domain.party.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
                .fetch().stream().map(pm -> PartyMemberDTO.from(pm)).toList();
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
    public Optional<PartyMember> findPartyPartiOrReqByMember(Party party, Member member) {
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
    public List<PartyMemberDTO> findAllParticipant(Party party, Member member) {

        QPartyMember qPartyMember = QPartyMember.partyMember;

        return jpaQueryFactory.selectFrom(qPartyMember)
                .where(qPartyMember.deletedAt.isNull()
                        , qPartyMember.role.eq(RoleType.PARTICIPANT)
                        , neMember(member, qPartyMember))
                .fetch()
                .stream().map(pm -> PartyMemberDTO.from(pm))
                .toList();
    }

    private BooleanExpression neMember(Member member, QPartyMember qPartyMember) {
        return member != null ? qPartyMember.member.ne(member) : null;
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

    @Override
    public Slice<MemberPartyHistoryResponseDTO> findAllPartyMemberByMember(Member member, RoleType roleType, Pageable pageable, boolean isAll) {
        QPartyMember qPartyMember = QPartyMember.partyMember;
        QParty qParty = QParty.party;

        List<Party> partyList = jpaQueryFactory.select(qParty)
                .from(qPartyMember)
                .where(qPartyMember.member.eq(member)
                        ,(qPartyMember.party.deletedAt.isNull())
                        , hasRole(qPartyMember, roleType, isAll)
                        , (qPartyMember.deletedAt.isNull()))
                .orderBy(getOrderSpecifier(pageable,qParty))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<MemberPartyHistoryResponseDTO> memberPartyHistoryResponseDTOList = new ArrayList<>();

        for (Party party : partyList) {
            List<PartyMemberDTO> partyMemberDTOList = jpaQueryFactory.selectFrom(qPartyMember)
                    .where(qPartyMember.party.eq(party)
                            .and(qPartyMember.role.ne(RoleType.REQUESTER))
                            .and(qPartyMember.deletedAt.isNull()))
                    .orderBy(qPartyMember.role.asc())
                    .fetch()
                    .stream().map(PartyMemberDTO::from)
                    .toList();

            memberPartyHistoryResponseDTOList.add(MemberPartyHistoryResponseDTO.of(party, partyMemberDTOList));
        }

        //Slice 생성
        boolean hasNext = memberPartyHistoryResponseDTOList.size() > pageable.getPageSize();
        if (hasNext)
            memberPartyHistoryResponseDTOList.removeLast();

        return new SliceImpl<>(memberPartyHistoryResponseDTOList, pageable, hasNext);
    }

    private BooleanExpression hasRole(QPartyMember qPartyMember, RoleType roleType, boolean isAll) {
        if (isAll)//전체 이용내역 검색일경우
            return qPartyMember != null ?  qPartyMember.role.eq(RoleType.ORGANIZER).or(qPartyMember.role.eq(RoleType.PARTICIPANT)).or(qPartyMember.role.eq(RoleType.REQUESTER)): null;
        else{
            //방장이 아닌 정보를 보여줄때 => 신청했지만 참여못한 파티팟에도
            if(roleType.equals(RoleType.PARTICIPANT)){
                return qPartyMember != null ? qPartyMember.role.eq(RoleType.PARTICIPANT).or(qPartyMember.role.eq(RoleType.REQUESTER)) : null;
            }
            return qPartyMember != null ? qPartyMember.role.eq(RoleType.ORGANIZER) : null;
        }
    }

    private OrderSpecifier getOrderSpecifier(Pageable pageable, QParty qParty) {
        OrderSpecifier orderSpecifier =null;

        for(Sort.Order order : pageable.getSort()){
            Order direction = order.getDirection().isAscending()? Order.ASC: Order.DESC;

            orderSpecifier = new OrderSpecifier(direction, qParty.createdAt);
        }

        return orderSpecifier;

    }
}
