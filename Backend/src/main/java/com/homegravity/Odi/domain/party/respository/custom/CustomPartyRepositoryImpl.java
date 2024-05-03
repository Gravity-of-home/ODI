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

    /**
     * 동승자 구인 글 목록 조회
     *
     * @param pageable   정렬 기준: 거리순, 출발 시간과 가까운 순
     * @param requestDTO 필터링 기준: 오늘 출발, 날짜, 성별, 카테고리, 모집 중
     * @return
     */
    @Override
    public List<Party> findAllParties(Pageable pageable, SelectPartyRequestDTO requestDTO) {
        QParty qparty = QParty.party;

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageable, qparty);

        // 지역범위: 전체
        return jpaQueryFactory.selectFrom(qparty)
                .where(qparty.deletedAt.isNull(), eqToday(requestDTO, qparty), eqGender(requestDTO, qparty), eqDepartureDate(requestDTO, qparty), eqCategory(requestDTO, qparty))
                .orderBy(orderSpecifier)
                .fetch();
    }

    private OrderSpecifier<?> getOrderSpecifier(Pageable pageable, QParty qparty) {
        OrderSpecifier<?> orderSpecifier = null;
        for (Sort.Order order : pageable.getSort()) {

            if (order.getProperty().equals("departuresDate")) {
                orderSpecifier = new OrderSpecifier<>(Order.DESC, qparty.departuresDate);
            } else if (order.getProperty().equals("distance")) {
                /* TODO: 거리순 정렬 로직 작성*/
            } else { // 정렬 기준이 없다면 최신순
                orderSpecifier = new OrderSpecifier<>(Order.DESC, qparty.createdAt);
            }
        }
        return orderSpecifier;
    }

    private BooleanExpression eqDepartureDate(SelectPartyRequestDTO requestDTO, QParty qparty) {
        LocalDate targetDate = requestDTO.getDeparturesDate();
        LocalDateTime from = LocalDateTime.of(targetDate, LocalTime.MIN);
        LocalDateTime to = LocalDateTime.of(targetDate, LocalTime.MAX);

        return requestDTO.getDeparturesDate() != null ? qparty.departuresDate.between(from, to) : null;
    }

    private BooleanExpression eqGender(SelectPartyRequestDTO requestDTO, QParty qparty) {
        return requestDTO.getGender() != null ? qparty.genderRestriction.eq(requestDTO.getGender()) : null;
    }

    private BooleanExpression eqToday(SelectPartyRequestDTO requestDTO, QParty qparty) {
        LocalDate today = LocalDate.now();
        LocalDateTime from = LocalDateTime.of(today, LocalTime.MIN);
        LocalDateTime to = LocalDateTime.of(today, LocalTime.MAX);
        return requestDTO.getIsToday() == true ? qparty.departuresDate.between(from, to) : null;
    }

    private BooleanExpression eqCategory(SelectPartyRequestDTO requestDTO, QParty qparty) {
        BooleanExpression eqCategory = requestDTO.getCategory() != null ? qparty.category.eq(requestDTO.getCategory()) : null;
        return eqCategory;
    }

}
