package com.homegravity.Odi.domain.place.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "장소 검색 DTO")
@Getter
@Setter
public class PlaceSearchDto {

    @Schema(description = "장소명")
    private String placeName;

    @Schema(description = "현재 위치 위도")
    private Double latitude;

    @Schema(description = "현재 위치 경도")
    private Double longitude;
}
