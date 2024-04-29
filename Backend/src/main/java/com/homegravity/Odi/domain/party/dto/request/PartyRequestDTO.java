package com.homegravity.Odi.domain.party.dto.request;

import com.homegravity.Odi.domain.party.dto.GeoPoint;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PartyRequestDTO {

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
    private LocalDateTime departuresDate;

    @NotNull
    private Integer maxParticipants;

    private String category;

    @NotNull
    private Boolean genderRestriction;

    private String content;

}
