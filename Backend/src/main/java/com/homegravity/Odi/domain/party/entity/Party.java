package com.homegravity.Odi.domain.party.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.homegravity.Odi.domain.match.dto.MatchRequestDTO;
import com.homegravity.Odi.domain.party.dto.LocationPoint;
import com.homegravity.Odi.domain.party.dto.request.PartyRequestDTO;
import com.homegravity.Odi.global.entity.BaseBy;
import jakarta.persistence.*;
import lombok.*;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "departures_date")
    private LocalDateTime departuresDate;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "current_participants")
    private Integer currentParticipants;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private CategoryType category;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_restriction")
    private GenderType genderRestriction;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private StateType state;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @OneToOne(mappedBy = "party", cascade = CascadeType.ALL)
    private PartyBoardStats partyBoardStats;

    @OneToOne(mappedBy = "party", cascade = CascadeType.ALL)
    private PartySettlement partySettlement;

    @Builder
    private Party(String title, String departuresName, Point departuresLocation,
                  String arrivalsName, Point arrivalsLocation,
                  LocalDateTime departuresDate, Integer maxParticipants, Integer currentParticipants,
                  CategoryType category, GenderType genderRestriction, String content) {

        this.title = title;
        this.departuresName = departuresName;
        this.departuresLocation = departuresLocation;
        this.arrivalsName = arrivalsName;
        this.arrivalsLocation = arrivalsLocation;
        this.departuresDate = departuresDate;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = currentParticipants;
        this.category = category;
        this.genderRestriction = genderRestriction;
        this.state = StateType.GATHERING;
        this.content = content;
    }

    public static Party of(MatchRequestDTO matchRequestDTO) {

        GeometryFactory geometryFactory = new GeometryFactory();
        Point departuresLocation = geometryFactory.createPoint(new Coordinate(matchRequestDTO.getDepLon(), matchRequestDTO.getDepLat()));
        Point arrivalsLocation = geometryFactory.createPoint(new Coordinate(matchRequestDTO.getArrLon(), matchRequestDTO.getArrLat()));

        return Party.builder()
                .title("[자동 매칭]")
                .departuresName(matchRequestDTO.getDepName())
                .departuresLocation(departuresLocation)
                .arrivalsName(matchRequestDTO.getArrName())
                .arrivalsLocation(arrivalsLocation)
                .maxParticipants(2)
                .currentParticipants(2)
                .genderRestriction(GenderType.ANY)
                .content("자동매칭에 성공했습니다.")
                .build();
    }

    public static Party of(PartyRequestDTO partyRequestDTO, String gender) {

        GeometryFactory geometryFactory = new GeometryFactory();
        Point departuresLocation = geometryFactory.createPoint(new Coordinate(partyRequestDTO.getDeparturesLocation().getLongitude(), partyRequestDTO.getDeparturesLocation().getLatitude()));
        Point arrivalsLocation = geometryFactory.createPoint(new Coordinate(partyRequestDTO.getArrivalsLocation().getLongitude(), partyRequestDTO.getArrivalsLocation().getLatitude()));

        GenderType genderRestriction = GenderType.ANY;
        if (partyRequestDTO.getGenderRestriction()) { // 성별 제한이 있다면

            if (gender.equals(GenderType.M.toString())) {
                genderRestriction = GenderType.M;
            } else if (gender.equals(GenderType.F.toString())) {
                genderRestriction = GenderType.F;
            }

        }

        return Party.builder()
                .title(partyRequestDTO.getTitle())
                .departuresName(partyRequestDTO.getDeparturesName())
                .departuresLocation(departuresLocation)
                .arrivalsName(partyRequestDTO.getArrivalsName())
                .arrivalsLocation(arrivalsLocation)
                .departuresDate(partyRequestDTO.getDeparturesDate())
                .maxParticipants(partyRequestDTO.getMaxParticipants())
                .currentParticipants(1) // 파티장
                .category(CategoryType.valueOf(partyRequestDTO.getCategory()))
                .genderRestriction(genderRestriction)
                .content(partyRequestDTO.getContent())
                .build();
    }

    public void updatePartyBoardStats(PartyBoardStats partyBoardStats) {
        this.partyBoardStats = partyBoardStats;
    }

    public void updatePartySettlement(PartySettlement partySettlement) {
        this.partySettlement = partySettlement;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateDeparturesName(String departuresName) {
        this.departuresName = departuresName;
    }

    public void updateArrivalsName(String arrivalsName) {
        this.arrivalsName = arrivalsName;
    }

    public void updateDeparturesLocation(LocationPoint departuresLocation) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(departuresLocation.getLongitude(), departuresLocation.getLatitude()));

        this.departuresLocation = point;
    }

    public void updateArrivalsLocation(LocationPoint arrivalsLocation) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(arrivalsLocation.getLongitude(), arrivalsLocation.getLatitude()));

        this.arrivalsLocation = point;
    }

    public void updateDeparturesDate(LocalDateTime departuresDate) {
        this.departuresDate = departuresDate;
    }

    public void updateMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public void updateCategory(String category) {
        this.category = CategoryType.valueOf(category);
    }

    public void updateGenderRestriction(GenderType genderRestriction) {
        this.genderRestriction = genderRestriction;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateState(StateType state) {
        this.state = state;
    }

    public void updateCurrentParticipants(int currentParticipants){ this.currentParticipants = currentParticipants; }

}
