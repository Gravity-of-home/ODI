package com.homegravity.Odi.domain.party.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.homegravity.Odi.domain.party.entity.CategoryType;
import com.homegravity.Odi.domain.party.entity.GenderType;
import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.entity.StateType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PartyDTO {

    private Long id;

    private CategoryType category;

    private GenderType genderRestriction; // M, F, ANY

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime modifiedAt;

    private String title;

    private String departuresName;

    private LocationPoint departuresLocation;

    private String arrivalsName;

    private LocationPoint arrivalsLocation;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departuresDate;

    private Integer maxParticipants;

    private Integer currentParticipants;

    private StateType state;

    private Integer viewCount;

    private Integer requestCount;

    private PartyMemberDTO organizer;

    @Builder
    private PartyDTO(Long id, CategoryType category, GenderType genderRestriction, LocalDateTime createAt, LocalDateTime modifiedAt, String title, String departuresName, LocationPoint departuresLocation, String arrivalsName, LocationPoint arrivalsLocation, LocalDateTime departuresDate, Integer maxParticipants, Integer currentParticipants, StateType state, Integer viewCount, Integer requestCount, PartyMemberDTO organizer) {
        this.id = id;
        this.category = category;
        this.genderRestriction = genderRestriction;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
        this.title = title;
        this.departuresName = departuresName;
        this.departuresLocation = departuresLocation;
        this.arrivalsName = arrivalsName;
        this.arrivalsLocation = arrivalsLocation;
        this.departuresDate = departuresDate;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = currentParticipants;
        this.state = state;
        this.viewCount = viewCount;
        this.requestCount = requestCount;
        this.organizer = organizer;
    }

    public static PartyDTO of(Party party, PartyMemberDTO organizer, int participantsCount) {

        LocationPoint departuresLocation = LocationPoint.of(party.getDeparturesLocation().getX(), party.getDeparturesLocation().getY());
        LocationPoint arrivalsLocation = LocationPoint.of(party.getArrivalsLocation().getX(), party.getArrivalsLocation().getY());

        return PartyDTO.builder()
                .id(party.getId())
                .category(party.getCategory())
                .genderRestriction(party.getGenderRestriction())
                .createAt(party.getCreatedAt())
                .modifiedAt(party.getModifiedAt())
                .title(party.getTitle())
                .departuresName(party.getDeparturesName())
                .departuresLocation(departuresLocation)
                .arrivalsName(party.getArrivalsName())
                .arrivalsLocation(arrivalsLocation)
                .departuresDate(party.getDeparturesDate())
                .maxParticipants(party.getMaxParticipants())
                .currentParticipants(participantsCount)
                .state(party.getState())
                .viewCount(party.getPartyBoardStats().getViewCount())
                .viewCount(party.getPartyBoardStats().getRequestCount())
                .organizer(organizer)
                .build();
    }

}
