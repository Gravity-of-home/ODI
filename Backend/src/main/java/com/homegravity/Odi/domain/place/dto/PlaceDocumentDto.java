package com.homegravity.Odi.domain.place.dto;

import com.homegravity.Odi.domain.party.dto.GeoPoint;
import com.homegravity.Odi.domain.place.entity.PlaceDocument;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "장소 DTO")
@Getter
@Setter
public class PlaceDocumentDto {

    @Schema(description = "장소 ID")
    private String id;

    @Schema(description = "장소명")
    private String placeName;

    @Schema(description = "건물명")
    private String buildingName;

    @Schema(description = "지번주소 (구주소)")
    private String jibunAddress;

    @Schema(description = "도로명주소 (신주소)")
    private String roadNameAddress;

    @Schema(description = "우편번호")
    private Integer postalCode;

    @Schema(description = "시도")
    private String sido;

    @Schema(description = "장소 위치 (위경도)")
    private GeoPoint geoPoint;

    @Builder
    private PlaceDocumentDto(String id, String placeName, String buildingName, String jibunAddress, String roadNameAddress, Integer postalCode, String sido, GeoPoint geoPoint) {
        this.id = id;
        this.placeName = placeName;
        this.buildingName = buildingName;
        this.jibunAddress = jibunAddress;
        this.roadNameAddress = roadNameAddress;
        this.postalCode = postalCode;
        this.sido = sido;
        this.geoPoint = geoPoint;
    }

    public static PlaceDocumentDto from(PlaceDocument placeDocument) {

        return builder()
                .id(placeDocument.getId())
                .placeName(placeDocument.getPlaceName())
                .buildingName(placeDocument.getBuildingName())
                .jibunAddress(placeDocument.getJibunAddress())
                .roadNameAddress(placeDocument.getRoadNameAddress())
                .postalCode(placeDocument.getPostalCode())
                .sido(placeDocument.getSido())
                .geoPoint(GeoPoint.of(placeDocument.getLatitude(), placeDocument.getLongitude()))
                .build();
    }
}
