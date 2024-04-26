package com.homegravity.Odi.domain.party.entity;

import com.homegravity.Odi.global.entity.BaseBy;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE party SET deleted_at = NOW() where party_id = ?")
public class Party extends BaseBy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long id;

    @Column(name = "title", length = 50)
    private String title;

    @Column(name = "departures_name", length = 50)
    private String departuresName;

    @Column(name = "departures_location", columnDefinition = "Point")
    private Point departuresLocation;

    @Column(name = "arrivals_name", length = 50)
    private String arrivalsName;

    @Column(name = "arrivals_location", columnDefinition = "Point")
    private Point arrivalsLocation;

    @Column(name = "expected_cost")
    private Integer expectedCost;

    @Column(name = "departures_date")
    private LocalDateTime departuresDate;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "category")
    private String category;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "state")
    private String state;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @OneToOne(mappedBy = "party", cascade = CascadeType.REMOVE)
    private PartyBoardStats partyBoardStats;

    @Builder
    private Party(String title, String departuresName, Point departuresLocation,
                  String arrivalsName, Point arrivalsLocation,
                  Integer expectedCost, LocalDateTime departuresDate, Integer maxParticipants,
                  String category, Boolean gender, String state, String content) {
        this.title = title;
        this.departuresName = departuresName;
        this.departuresLocation = departuresLocation;
        this.arrivalsName = arrivalsName;
        this.arrivalsLocation = arrivalsLocation;
        this.expectedCost = expectedCost;
        this.departuresDate = departuresDate;
        this.maxParticipants = maxParticipants;
        this.category = category;
        this.gender = gender;
        this.state = state;
        this.content = content;
    }

    public static Party of(String title, String departuresName, Point departuresLocation,
                           String arrivalsName, Point arrivalsLocation,
                           Integer expectedCost, LocalDateTime departuresDate, Integer maxParticipants,
                           String category, Boolean gender, String state, String content) {

        return Party.builder()
                .title(title)
                .departuresName(departuresName)
                .departuresLocation(departuresLocation)
                .arrivalsName(arrivalsName)
                .arrivalsLocation(arrivalsLocation)
                .expectedCost(expectedCost)
                .departuresDate(departuresDate)
                .maxParticipants(maxParticipants)
                .category(category)
                .gender(gender)
                .state(state)
                .content(content)
                .build();

    }

    public void updatePartyBoardStats(PartyBoardStats partyBoardStats) {
        this.partyBoardStats = partyBoardStats;
    }

    public void updateDeparturesName(String departuresName) {
        this.departuresName = departuresName;
    }

    public void updateArrivalsName(String arrivalsName) {
        this.arrivalsName = arrivalsName;
    }

    public void updateDeparturesLocation(Point departuresLocation) {
        this.departuresLocation = departuresLocation;
    }

    public void updateArrivalsLocation(Point arrivalsLocation) {
        this.arrivalsLocation = arrivalsLocation;
    }

}
