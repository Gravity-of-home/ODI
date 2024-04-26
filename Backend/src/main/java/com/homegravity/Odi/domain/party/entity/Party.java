package com.homegravity.Odi.domain.party.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.homegravity.Odi.domain.party.dto.request.PartyRequestDTO;
import com.homegravity.Odi.global.entity.BaseBy;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "expected_duration")
    private LocalDateTime expectedDuration;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "departures_date")
    private LocalDateTime departuresDate;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "category")
    private String category;

    @Column(name = "gender_restriction")
    private String genderRestriction;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private StateType state;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @OneToOne(mappedBy = "party", cascade = CascadeType.ALL)
    private PartyBoardStats partyBoardStats;

    @Builder
    private Party(String title, String departuresName, Point departuresLocation,
                  String arrivalsName, Point arrivalsLocation,
                  Integer expectedCost, LocalDateTime expectedDuration,
                  LocalDateTime departuresDate, Integer maxParticipants,
                  String category, String genderRestriction, String content) {

        this.title = title;
        this.departuresName = departuresName;
        this.departuresLocation = departuresLocation;
        this.arrivalsName = arrivalsName;
        this.arrivalsLocation = arrivalsLocation;
        this.expectedCost = expectedCost;
        this.expectedDuration = expectedDuration;
        this.departuresDate = departuresDate;
        this.maxParticipants = maxParticipants;
        this.category = category;
        this.genderRestriction = genderRestriction;
        this.state = StateType.GATHERING;
        this.content = content;
    }

    public static Party of(String title, String departuresName, Point departuresLocation,
                           String arrivalsName, Point arrivalsLocation,
                           Integer expectedCost, LocalDateTime expectedDuration,
                           LocalDateTime departuresDate, Integer maxParticipants,
                           String category, String genderRestriction, String content) {

        return Party.builder()
                .title(title)
                .departuresName(departuresName)
                .departuresLocation(departuresLocation)
                .arrivalsName(arrivalsName)
                .arrivalsLocation(arrivalsLocation)
                .expectedCost(expectedCost)
                .expectedDuration(expectedDuration)
                .departuresDate(departuresDate)
                .maxParticipants(maxParticipants)
                .category(category)
                .genderRestriction(genderRestriction)
                .content(content)
                .build();

    }

    public static Party of(PartyRequestDTO partyRequestDTO, String gender) {

        GeometryFactory geometryFactory = new GeometryFactory();
        Point departuresLocation = geometryFactory.createPoint(new Coordinate(partyRequestDTO.getDeparturesLocation().getX(), partyRequestDTO.getDeparturesLocation().getY()));
        Point arrivalsLocation = geometryFactory.createPoint(new Coordinate(partyRequestDTO.getArrivalsLocation().getX(), partyRequestDTO.getArrivalsLocation().getY()));

        String genderRestriction = "ANY";
        if (partyRequestDTO.getGenderRestriction()) { // 성별 제한이 있다면
            genderRestriction = gender;
        }

        return Party.builder()
                .title(partyRequestDTO.getTitle())
                .departuresName(partyRequestDTO.getDeparturesName())
                .departuresLocation(departuresLocation)
                .arrivalsName(partyRequestDTO.getArrivalsName())
                .arrivalsLocation(arrivalsLocation)
                .expectedCost(partyRequestDTO.getExpectedCost())
                .expectedDuration(partyRequestDTO.getExpectedDuration())
                .departuresDate(partyRequestDTO.getDeparturesDate())
                .maxParticipants(partyRequestDTO.getMaxParticipants())
                .category(partyRequestDTO.getCategory())
                .genderRestriction(genderRestriction)
                .content(partyRequestDTO.getContent())
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

    public void updateState(StateType state) {
        this.state = state;
    }

}
