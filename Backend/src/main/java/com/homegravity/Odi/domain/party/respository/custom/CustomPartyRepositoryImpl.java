package com.homegravity.Odi.domain.party.respository.custom;

import com.homegravity.Odi.domain.party.dto.request.SelectPartyRequestDTO;
import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.entity.QParty;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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
    public Slice<Party> findAllParties(Pageable pageable, SelectPartyRequestDTO requestDTO) {
        QParty qparty = QParty.party;

        OrderSpecifier orderSpecifier = getOrderSpecifier(pageable, qparty);

        // 지역범위: 전체
        List<Party> results = jpaQueryFactory.selectFrom(qparty)
                .where(qparty.deletedAt.isNull(), qparty.departuresDate.goe(LocalDateTime.now())
                        , eqToday(requestDTO, qparty)
                        , eqGender(requestDTO, qparty)
                        , eqDepartureDate(requestDTO, qparty)
                        , eqCategory(requestDTO, qparty)
                )
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Slice 생성
        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) {
            results.remove(results.size() - 1);
        }

        log.info("Number of parties fetched: {}", results.size());

        return new SliceImpl<>(results, pageable, hasNext);
    }

    private OrderSpecifier getOrderSpecifier(Pageable pageable, QParty qparty) {
        OrderSpecifier orderSpecifier = null;
        for (Sort.Order order : pageable.getSort()) {

            Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

            if (order.getProperty().equals("departuresDate")) { // 출발 시간 가까운 순

                // 현재 시간과의 시간 차이를 계산하는 표현식
                NumberExpression<Integer> timeDifference = Expressions.numberTemplate(
                        Integer.class,
                        "TIMESTAMPDIFF(SECOND, NOW(), {0})",
                        qparty.departuresDate
                );

                orderSpecifier = new OrderSpecifier(direction, timeDifference);


            } else if (order.getProperty().equals("distance")) {
                /* TODO: 거리순 정렬 로직 작성*/
            } else { // 정렬 기준이 없다면 최신순
                orderSpecifier = new OrderSpecifier(Order.DESC, qparty.createdAt);
            }
        }
        return orderSpecifier;
    }

    private BooleanExpression eqDepartureDate(SelectPartyRequestDTO requestDTO, QParty qparty) {

        BooleanExpression condition = null;

        if (requestDTO.getDeparturesDate() != null) {

            LocalDate targetDate = requestDTO.getDeparturesDate();
            LocalDateTime from = LocalDateTime.of(targetDate, LocalTime.MIN);
            LocalDateTime to = LocalDateTime.of(targetDate, LocalTime.MAX);

            condition = qparty.departuresDate.between(from, to);

            log.info("출발 날짜 조건 {} 출발이요 : {}", targetDate, condition);
        }


        return condition;
    }

    private BooleanExpression eqGender(SelectPartyRequestDTO requestDTO, QParty qparty) {
        BooleanExpression condition = requestDTO.getGender() != null ? qparty.genderRestriction.eq(requestDTO.getGender()) : null;
        log.info("성별 조건 == {}", condition);
        return condition;
    }

    private BooleanExpression eqToday(SelectPartyRequestDTO requestDTO, QParty qparty) {
        LocalDate today = LocalDate.now();
        LocalDateTime from = LocalDateTime.of(today, LocalTime.MIN);
        LocalDateTime to = LocalDateTime.of(today, LocalTime.MAX);
        BooleanExpression condition = requestDTO.getIsToday() == true ? qparty.departuresDate.between(from, to) : null;

        log.info("오늘 출발 조건 : {}", condition);
        return condition;
    }

    private BooleanExpression eqCategory(SelectPartyRequestDTO requestDTO, QParty qparty) {
        BooleanExpression condition = requestDTO.getCategory() != null ? qparty.category.eq(requestDTO.getCategory()) : null;

        log.info("카테고리 조건 : {}", condition);
        return condition;
    }

}
