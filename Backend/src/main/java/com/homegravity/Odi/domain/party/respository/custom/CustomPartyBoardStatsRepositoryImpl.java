package com.homegravity.Odi.domain.party.respository.custom;

import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.entity.PartyBoardStats;
import com.homegravity.Odi.domain.party.entity.QPartyBoardStats;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomPartyBoardStatsRepositoryImpl implements CustomPartyBoardStatsRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public PartyBoardStats findPartyBoardStats(Party party) {

        QPartyBoardStats qPartyBoardStats = QPartyBoardStats.partyBoardStats;
        return jpaQueryFactory.selectFrom(qPartyBoardStats)
                .where(qPartyBoardStats.party.eq(party)
                        .and(qPartyBoardStats.deletedAt.isNull()))
                .fetchOne();
    }
}
