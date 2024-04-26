package com.homegravity.Odi.domain.party.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.homegravity.Odi.domain.party.dto.GeoPoint;
import com.homegravity.Odi.domain.party.dto.PartyMemberDTO;
import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.entity.PartyBoardStats;
import com.homegravity.Odi.domain.party.entity.RoleType;
import com.homegravity.Odi.domain.party.entity.StateType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PartyResponseDTO {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String departuresName;

    @NotNull
    private GeoPoint departuresLocation;

    @NotNull
    private String arrivalsName;

    @NotNull
    private GeoPoint arrivalsLocation;

    @NotNull
    private Integer expectedCost;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime expectedDuration;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departuresDate;

    @NotNull
    private Integer maxParticipants;

    private String category;

    private Boolean gender;

    @NotNull
    private StateType state;

    private String content;

    @NotNull
    private Integer viewCount;

    @NotNull
    private Integer requestCount;

    private RoleType role; // 상세 페이지 요청자의 역할

    private List<PartyMemberDTO> participants;

    private List<PartyMemberDTO> guests;

    @Builder
    private PartyResponseDTO(Long id, String title, String departuresName, GeoPoint departuresLocation,
                             String arrivalsName, GeoPoint arrivalsLocation, Integer expectedCost, LocalDateTime expectedDuration,
                             LocalDateTime departuresDate, Integer maxParticipants, String category,
                             Boolean gender, StateType state, String content,
                             Integer viewCount, Integer requestCount,
                             RoleType role, List<PartyMemberDTO> participants, List<PartyMemberDTO> guests) {
        this.id = id;
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
        this.gender = gender;
        this.state = state;
        this.content = content;
        this.viewCount = viewCount;
        this.requestCount = requestCount;
        this.role = role;
        this.participants = participants;
        this.guests = guests;
    }

    public static PartyResponseDTO of(Party party, PartyBoardStats partyBoardStats, RoleType role,
                                      List<PartyMemberDTO> participants, List<PartyMemberDTO> guests) {

        GeoPoint departuresLocation = GeoPoint.of(party.getDeparturesLocation().getX(), party.getDeparturesLocation().getY());
        GeoPoint arrivalsLocation = GeoPoint.of(party.getArrivalsLocation().getX(), party.getDeparturesLocation().getY());

        return PartyResponseDTO.builder()
                .id(party.getId())
                .title(party.getTitle())
                .departuresName(party.getDeparturesName())
                .departuresLocation(departuresLocation)
                .arrivalsName(party.getArrivalsName())
                .arrivalsLocation(arrivalsLocation)
                .expectedCost(party.getExpectedCost())
                .expectedDuration(party.getExpectedDuration())
                .departuresDate(party.getDeparturesDate())
                .maxParticipants(party.getMaxParticipants())
                .category(party.getCategory())
                .gender(party.getGender())
                .state(party.getState())
                .content(party.getContent())
                .viewCount(partyBoardStats.getViewCount())
                .requestCount(partyBoardStats.getRequestCount())
                .role(role)
                .participants(participants)
                .guests(guests)
                .build();
    }

}
