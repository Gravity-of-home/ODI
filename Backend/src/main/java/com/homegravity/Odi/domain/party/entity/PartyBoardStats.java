package com.homegravity.Odi.domain.party.entity;

import com.homegravity.Odi.global.entity.BaseBy;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE party_board_stats SET deleted_at = NOW() where party_id = ?")
public class PartyBoardStats extends BaseBy {

    @Id
    @Column(name = "party_id")
    private Long id;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "request_count")
    private Integer requestCount;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // partyId를 party의 id와 매핑
    @JoinColumn(name = "party_id")
    private Party party;

    @Builder
    private PartyBoardStats(Integer viewCount, Integer requestCount) {
        this.viewCount = viewCount;
        this.requestCount = requestCount;
    }

    public static PartyBoardStats of(Integer viewCount, Integer requestCount) {
        return builder()
                .viewCount(viewCount)
                .requestCount(requestCount)
                .build();
    }

    public void updateViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public void updateRequestCount(Integer requestCount) {
        this.requestCount = requestCount;
    }

    // 연관관계 편의 메서드
    public void updateParty(Party party) {

        this.party = party;

        if (party.getPartyBoardStats() != this) {
            party.updatePartyBoardStats(this);
        }

    }

}
