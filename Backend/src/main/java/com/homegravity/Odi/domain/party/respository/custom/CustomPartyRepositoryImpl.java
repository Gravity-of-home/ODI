package com.homegravity.Odi.domain.party.respository.custom;

import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.entity.QParty;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public List<Party> findAllParties() {
        /* Querydsl 작성 */
        return null;
    }

    @Override
    public List<Party> findPopularParties() {
        /* Querydsl 작성 */
        return null;
    }
}
