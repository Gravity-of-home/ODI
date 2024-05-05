package com.homegravity.Odi.domain.party.dto.request;

import com.homegravity.Odi.domain.party.dto.LocationPoint;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class PartyRequestDTO {

    private String title;

    private String departuresName;

    private LocationPoint departuresLocation;

    private String arrivalsName;

    private LocationPoint arrivalsLocation;

    private LocalDateTime departuresDate;

    private Integer maxParticipants;

    private String category;

    private Boolean genderRestriction;

    private String content;

}
