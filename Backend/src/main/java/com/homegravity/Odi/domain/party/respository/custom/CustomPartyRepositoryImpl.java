package com.homegravity.Odi.domain.party.respository.custom;

import com.homegravity.Odi.domain.party.dto.PartyDTO;
import com.homegravity.Odi.domain.party.dto.request.SelectPartyRequestDTO;
import com.homegravity.Odi.domain.party.entity.CategoryType;
import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.entity.QParty;
import com.homegravity.Odi.domain.party.entity.StateType;
import com.homegravity.Odi.domain.party.respository.PartyMemberRepository;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.dsl.Expressions.numberTemplate;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomPartyRepositoryImpl implements CustomPartyRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final PartyMemberRepository partyMemberRepository;

    @Override
    public Optional<Party> findParty(Long partyId) {
        QParty qParty = QParty.party;
        return Optional.ofNullable(jpaQueryFactory.selectFrom(qParty)
                .where(qParty.id.eq(partyId)
                        .and(qParty.deletedAt.isNull()))
                .fetchOne());
    }

    /**
     * 동승자 구인 글 목록 조회
     *
     * @param pageable   정렬 기준: 거리순, 출발 시간과 가까운 순
     * @param requestDTO 필터링 기준: 오늘 출발, 날짜, 성별, 카테고리, 모집 중
     * @return
     */
    @Override
    public Slice<PartyDTO> findAllParties(Pageable pageable, SelectPartyRequestDTO requestDTO) {
        QParty qparty = QParty.party;

        // 거리 계산 수식
        NumberExpression<Double> distance = numberTemplate(Double.class,
                "calculate_distance({0}, {1}, {2})",
                requestDTO.getLatitude(), requestDTO.getLongitude(), qparty.departuresLocation);

        // 정렬 조건
        OrderSpecifier orderSpecifier = getOrderSpecifier(pageable, qparty, distance);

        // 지역범위: 현재 위치
        Double radius = 2.0; // 검색 반경 2km == 도보 20분
        List<Tuple> results = jpaQueryFactory.select(distance, qparty)
                .from(qparty)
                .where(qparty.deletedAt.isNull()
                        , qparty.category.ne(CategoryType.MATCHING)
                        , qparty.departuresDate.goe(LocalDateTime.now()) // 현재 시간 이후만 조회
                        , distance.loe(radius) //현재 위치로 부터 반경 2km
                        , eqToday(requestDTO, qparty)
                        , eqGender(requestDTO, qparty)
                        , eqDepartureDate(requestDTO, qparty)
                        , eqCategory(requestDTO, qparty)
                )
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<PartyDTO> partyList = new ArrayList<>();

        for (Tuple tuple : results) {
            Double distanceValue = tuple.get(distance);
            Party party = tuple.get(qparty);
            partyList.add(PartyDTO.of(party, partyMemberRepository.findOrganizer(party)
                    .orElseThrow(() -> new BusinessException(ErrorCode.PARTY_MEMBER_NOT_EXIST, ErrorCode.PARTY_MEMBER_NOT_EXIST.getMessage())), distanceValue));
        }

        // Slice 생성
        boolean hasNext = partyList.size() > pageable.getPageSize();
        if (hasNext) {
            partyList.remove(partyList.size() - 1);
        }

        log.info("Number of parties fetched: {}", results.size());

        return new SliceImpl<>(partyList, pageable, hasNext);
    }

    private OrderSpecifier getOrderSpecifier(Pageable pageable, QParty qparty, NumberExpression<Double> distance) {
        OrderSpecifier orderSpecifier = null;
        for (Sort.Order order : pageable.getSort()) {

            Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

            if (order.getProperty().equals("departuresDate")) { // 출발 시간 가까운 순

                // 현재 시간과의 시간 차이를 계산하는 표현식
                NumberExpression<Integer> timeDifference = numberTemplate(
                        Integer.class,
                        "TIMESTAMPDIFF(SECOND, NOW(), {0})",
                        qparty.departuresDate
                );

                orderSpecifier = new OrderSpecifier(direction, timeDifference);


            } else if (order.getProperty().equals("distance")) { // 출발지 가까운 순

                orderSpecifier = new OrderSpecifier(direction, distance);

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

    //정산 중, 정산다된 모든 party 찾기
    @Override
    public List<Party> findAllPartiedsSettlingSettled(){
        QParty qparty = QParty.party;

        return jpaQueryFactory.selectFrom(qparty)
                .where(qparty.state.eq(StateType.SETTLED)
                        .or(qparty.state.eq(StateType.SETTLING))
                        .and(qparty.deletedAt.isNull()))
                .fetch();
    }

}
