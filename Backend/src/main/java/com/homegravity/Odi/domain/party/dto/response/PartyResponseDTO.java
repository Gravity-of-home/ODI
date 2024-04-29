package com.homegravity.Odi.domain.party.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.homegravity.Odi.domain.party.dto.GeoPoint;
import com.homegravity.Odi.domain.party.dto.PartyMemberDTO;
import com.homegravity.Odi.domain.party.entity.*;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH-mm")
    private LocalDateTime createAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH-mm")
    private LocalDateTime modifiedAt;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departuresDate;

    private Integer maxParticipants;

    private Integer currentParticipants;

    private String category;

    private GenderType genderRestriction; // M, F, ANY

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
    private PartyResponseDTO(Long id, String title, LocalDateTime createAt, LocalDateTime modifiedAt,
                             String departuresName, GeoPoint departuresLocation, String arrivalsName, GeoPoint arrivalsLocation,
                             LocalDateTime departuresDate, Integer maxParticipants, Integer currentParticipants, String category,
                             GenderType genderRestriction, StateType state, String content,
                             Integer viewCount, Integer requestCount,
                             RoleType role, List<PartyMemberDTO> participants, List<PartyMemberDTO> guests) {

        this.id = id;
        this.title = title;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
        this.departuresName = departuresName;
        this.departuresLocation = departuresLocation;
        this.arrivalsName = arrivalsName;
        this.arrivalsLocation = arrivalsLocation;
        this.departuresDate = departuresDate;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = currentParticipants;
        this.category = category;
        this.genderRestriction = genderRestriction;
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
                .createAt(party.getCreatedAt())
                .modifiedAt(party.getModifiedAt())
                .title(party.getTitle())
                .departuresName(party.getDeparturesName())
                .departuresLocation(departuresLocation)
                .arrivalsName(party.getArrivalsName())
                .arrivalsLocation(arrivalsLocation)
                .departuresDate(party.getDeparturesDate())
                .maxParticipants(party.getMaxParticipants())
                .maxParticipants(participants.size())
                .category(party.getCategory())
                .genderRestriction(party.getGenderRestriction())
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
