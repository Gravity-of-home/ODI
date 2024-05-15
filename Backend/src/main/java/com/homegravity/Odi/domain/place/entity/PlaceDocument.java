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

    @Field(type = FieldType.Text, analyzer = "suggest_index_analyzer", searchAnalyzer = "suggest_search_analyzer", name = "place_name")
    private String placeName;

    @Field(type = FieldType.Text, name = "building_name")
    private String buildingName;

    @Field(type = FieldType.Text, name = "jibun_address")
    private String jibunAddress;

    @Field(type = FieldType.Text, name = "road_name_address")
    private String roadNameAddress;

    @Field(type = FieldType.Integer, name = "postal_code")
    private Integer postalCode;

    @Field(type = FieldType.Text, name="sido")
    private String sido;

    @Field(type = FieldType.Text, name="sigungu")
    private String sigungu;

    @Field(type = FieldType.Double, name="latitude")
    private Double latitude;

    @Field(type = FieldType.Double, name="longitude")
    private Double longitude;

    @Field(type= FieldType.Text, name="major_category")
    private String majorCategory;

    @Field(type= FieldType.Text, name="sub_category")
    private String subCategory;

    @Field(type= FieldType.Text, name="busstop_name")
    private String busstopName;

    @Field(type = FieldType.Integer, name = "bus_stop_num")
    private Integer busstopNum;

    @GeoPointField
    private GeoPoint locationGeopoint;

}
