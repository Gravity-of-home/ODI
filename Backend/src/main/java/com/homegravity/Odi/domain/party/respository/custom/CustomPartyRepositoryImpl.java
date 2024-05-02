package com.homegravity.Odi.domain.party.respository.custom;

import com.homegravity.Odi.domain.party.dto.request.SelectPartyRequestDTO;
import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.entity.QParty;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomPartyRepositoryImpl implements CustomPartyRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Party findParty(Long partyId) {
        QParty qParty = QParty.party;
        return jpaQueryFactory.selectFrom(qParty)
                .where(qParty.id.eq(partyId)
                        .and(qParty.deletedAt.isNull()))
                .fetchOne();
    }

    @Override
    public List<Party> findAllParties(Pageable pageable, SelectPartyRequestDTO requestDTO) {
        QParty qparty = QParty.party;

        // 필터링 기준: 오늘 출발, 날짜, 성별, 카테고리, 모집 중
        LocalDate today = LocalDate.now();
        LocalDateTime from = LocalDateTime.of(today, LocalTime.MIN);
        LocalDateTime to = LocalDateTime.of(today, LocalTime.MAX);

        BooleanExpression eqToday = requestDTO.isToday() == true ? qparty.departuresDate.between(from, to) : null;
        BooleanExpression eqGender = requestDTO.getGender() != null ? qparty.genderRestriction.eq(requestDTO.getGender()) : null;
        BooleanExpression eqDate = requestDTO.getDeparturesDate() != null ? qparty.departuresDate.eq(requestDTO.getDeparturesDate()) : null;
        BooleanExpression eqCategory = requestDTO.getCategory() != null ? qparty.category.eq(requestDTO.getCategory()) : null;


        // 정렬 기준: 거리순, 출발 시간과 가까운 순
        OrderSpecifier<?> orderSpecifier = null;
        for (Sort.Order order : pageable.getSort()) {

            if (order.getProperty().equals("departuresDate")) {
                orderSpecifier = new OrderSpecifier<>(Order.DESC, qparty.departuresDate);
            } else if (order.getProperty().equals("distance")) {
                /* 로직 작성*/
            } else { // 정렬 기준이 없다면 최신순
                orderSpecifier = new OrderSpecifier<>(Order.DESC, qparty.createdAt);
            }
        }

        // 지역범위: 전체
        return jpaQueryFactory.selectFrom(qparty)
                .where(qparty.deletedAt.isNull(), eqToday, eqGender, eqDate, eqCategory)
                .orderBy(orderSpecifier)
                .fetch();
    }

    @Override
    public List<Party> findPopularParties() {
        /* Querydsl 작성 */
        return null;
    }
}
