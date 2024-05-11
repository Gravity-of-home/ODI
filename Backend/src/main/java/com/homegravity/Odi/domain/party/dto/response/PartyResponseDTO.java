package com.homegravity.Odi.domain.party.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.homegravity.Odi.domain.party.dto.LocationPoint;
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

    private String roomId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime modifiedAt;

    @NotNull
    private String title;

    @NotNull
    private String departuresName;

    @NotNull
    private LocationPoint departuresLocation;

    @NotNull
    private String arrivalsName;

    @NotNull
    private LocationPoint arrivalsLocation;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departuresDate;

    private Integer maxParticipants;

    private Integer currentParticipants;

    private CategoryType category;

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

    private String pathInfo;

    @Builder
    private PartyResponseDTO(Long id, String roomId, String title, LocalDateTime createAt, LocalDateTime modifiedAt,
                             String departuresName, LocationPoint departuresLocation, String arrivalsName, LocationPoint arrivalsLocation,
                             LocalDateTime departuresDate, Integer maxParticipants, Integer currentParticipants, CategoryType category,
                             GenderType genderRestriction, StateType state, String content,
                             Integer viewCount, Integer requestCount,
                             RoleType role, List<PartyMemberDTO> participants, List<PartyMemberDTO> guests, String pathInfo) {

        this.id = id;
        this.roomId = roomId;
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
        this.pathInfo = pathInfo;
    }

    public static PartyResponseDTO of(Party party, PartyBoardStats partyBoardStats, RoleType role,
                                      List<PartyMemberDTO> participants, List<PartyMemberDTO> guests, String pathInfo) {

        LocationPoint departuresLocation = LocationPoint.of(party.getDeparturesLocation().getX(), party.getDeparturesLocation().getY());
        LocationPoint arrivalsLocation = LocationPoint.of(party.getArrivalsLocation().getX(), party.getArrivalsLocation().getY());

        return PartyResponseDTO.builder()
                .id(party.getId())
                .roomId(party.getRoomId())
                .createAt(party.getCreatedAt())
                .modifiedAt(party.getModifiedAt())
                .title(party.getTitle())
                .departuresName(party.getDeparturesName())
                .departuresLocation(departuresLocation)
                .arrivalsName(party.getArrivalsName())
                .arrivalsLocation(arrivalsLocation)
                .departuresDate(party.getDeparturesDate())
                .maxParticipants(party.getMaxParticipants())
                .currentParticipants(participants.size())
                .category(party.getCategory())
                .genderRestriction(party.getGenderRestriction())
                .state(party.getState())
                .content(party.getContent())
                .viewCount(partyBoardStats.getViewCount())
                .requestCount(partyBoardStats.getRequestCount())
                .role(role)
                .participants(participants)
                .guests(guests)
                .pathInfo(pathInfo)
                .build();
    }

}
