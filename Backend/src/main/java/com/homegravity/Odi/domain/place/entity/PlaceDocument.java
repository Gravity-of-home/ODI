package com.homegravity.Odi.domain.place.entity;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Document(indexName = "place_index")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, fielddata = true, analyzer = "suggest_index_analyzer", searchAnalyzer = "suggest_search_analyzer", name = "place_name")
    private String placeName;

    @Field(type = FieldType.Text, fielddata = true, name = "building_name")
    private String buildingName;

    @Field(type = FieldType.Text, fielddata = true, name = "jibun_address")
    private String jibunAddress;

    @Field(type = FieldType.Text, fielddata = true, name = "road_name_address")
    private String roadNameAddress;

    @Field(type = FieldType.Integer, name = "postal_code")
    private Integer postalCode;

    @Field(type = FieldType.Text, fielddata = true, name="sido")
    private String sido;

    @Field(type = FieldType.Text, fielddata = true, name="sigungu")
    private String sigungu;

    @Field(type = FieldType.Double, fielddata = true, name="latitude")
    private Double latitude;

    @Field(type = FieldType.Double, fielddata = true, name="longitude")
    private Double longitude;

    @GeoPointField
    private GeoPoint locationGeopoint;

    @Builder
    private PlaceDocument(String placeName, String buildingName, String jibunAddress, String roadNameAddress, Integer postalCode, String sido, String sigungu, Double latitude, Double longitude, GeoPoint locationGeopoint) {
        this.placeName = placeName;
        this.buildingName = buildingName;
        this.jibunAddress = jibunAddress;
        this.roadNameAddress = roadNameAddress;
        this.postalCode = postalCode;
        this.sido = sido;
        this.sigungu = sigungu;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationGeopoint = locationGeopoint;
    }

    public static PlaceDocument of(String placeName, String buildingName, String jibunAddress, String roadNameAddress, Integer postalCode, String sido, String sigungu, Double latitude, Double longitude, GeoPoint locationGeopoint) {

        return builder()
                .placeName(placeName)
                .buildingName(buildingName)
                .jibunAddress(jibunAddress)
                .roadNameAddress(roadNameAddress)
                .postalCode(postalCode)
                .sido(sido)
                .sigungu(sigungu)
                .latitude(latitude)
                .longitude(longitude)
                .locationGeopoint(locationGeopoint)
                .build();
    }
}
