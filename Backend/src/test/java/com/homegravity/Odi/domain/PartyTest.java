package com.homegravity.Odi.domain;

import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.entity.PartyBoardStats;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@Commit
public class PartyTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void testEntity() {
        Party partyA = Party.builder().title("partyA").build();
        Party partyB = Party.builder().title("partyB").build();

        em.persist(partyA);
        em.persist(partyB);

        PartyBoardStats partyBoardStatsA = PartyBoardStats.of(1, 1);
        PartyBoardStats partyBoardStatsB = PartyBoardStats.of(1, 2);

        partyBoardStatsA.updateParty(partyA);
        partyBoardStatsB.updateParty(partyB);


        em.persist(partyBoardStatsA);
        em.persist(partyBoardStatsB);

        //초기화
        em.flush();
        em.clear();

        //확인
        List<PartyBoardStats> partyBoardStatsList = em.createQuery("SELECT p FROM PartyBoardStats p", PartyBoardStats.class).getResultList();

        for (PartyBoardStats partyBoardStats : partyBoardStatsList) {
            System.out.println("PartyBoardStats = " + partyBoardStats.getRequestCount());
            System.out.println("=> party = " + partyBoardStats.getParty().getTitle());
        }
    }


    @Test
    public void testCascadeDelete() {
        // 엔티티 생성 및 관계 설정
        Party party = Party.builder().title("Cascade Test Party").build();
        PartyBoardStats stats = PartyBoardStats.builder().viewCount(100).requestCount(50).build();
        stats.updateParty(party); // 연결 설정

        em.persist(party);
        em.persist(stats);

        em.flush(); // JPA의 영속성 컨텍스트에 존재하는 모든 변경사항을 DB에 반영
        em.clear(); // 영속성 컨텍스트를 비워서 모든 엔티티를 1차 캐시에서 제거

        // Party 삭제
        em.remove(em.find(Party.class, party.getId()));
        em.flush();

        // 삭제 확인
        PartyBoardStats deletedStats = em.find(PartyBoardStats.class, stats.getId());
        System.out.println(deletedStats.getDeletedAt());
    }

}
