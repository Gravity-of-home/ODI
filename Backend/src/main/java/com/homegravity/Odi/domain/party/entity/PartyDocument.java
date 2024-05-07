package com.homegravity.Odi.domain.party.entity;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.time.LocalDateTime;

@Document(indexName = "party_index")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartyDocument {

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Text, name = "title", analyzer = "my_nori_analyzer")
    private String title;

    @Field(type = FieldType.Text, fielddata = true, name = "departures_name", analyzer = "my_nori_analyzer")
    private String departuresName;

    @GeoPointField
    private GeoPoint departuresLocation;

    @Field(type = FieldType.Text, fielddata = true, name = "arrivals_name", analyzer = "my_nori_analyzer")
    private String arrivalsName;

    @GeoPointField
    private GeoPoint arrivalsLocation;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second, name = "departures_date")
    private LocalDateTime departuresDate;

    @Field(type = FieldType.Integer, name = "max_participants")
    private Integer maxParticipants;

    @Field(type = FieldType.Integer, name = "current_participants")
    private Integer currentParticipants;

    @Field(type = FieldType.Keyword, name = "category")
    private String category;

    @Field(type = FieldType.Keyword, name = "gender_restriction")
    private String genderRestriction;

    @Field(type = FieldType.Keyword, name = "state")
    private String state;

    @Field(type = FieldType.Text, name = "content", analyzer = "my_nori_analyzer")
    private String content;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second, name = "created_at")
    private LocalDateTime createdAt;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second, name = "modified_at")
    private LocalDateTime modifiedAt;

    @Builder
    private PartyDocument(Long id, String title, String departuresName, GeoPoint departuresLocation, String arrivalsName, GeoPoint arrivalsLocation, LocalDateTime departuresDate, Integer maxParticipants, Integer currentParticipants, String category, String genderRestriction, String state, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
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
        this.state = state;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static PartyDocument from(Party party) {
        return builder()
                .id(party.getId())
                .title(party.getTitle())
                .departuresName(party.getDeparturesName())
                .departuresLocation(new GeoPoint(party.getDeparturesLocation().getY(), party.getDeparturesLocation().getX()))
                .arrivalsName(party.getArrivalsName())
                .arrivalsLocation(new GeoPoint(party.getArrivalsLocation().getY(), party.getArrivalsLocation().getX()))
                .departuresDate(party.getDeparturesDate())
                .maxParticipants(party.getMaxParticipants())
                .currentParticipants(party.getCurrentParticipants())
                .category(party.getCategory().getDescription())
                .genderRestriction(party.getGenderRestriction().toString())
                .state(party.getState().toString())
                .content(party.getContent())
                .createdAt(party.getCreatedAt())
                .modifiedAt(party.getModifiedAt())
                .build();
    }
}
