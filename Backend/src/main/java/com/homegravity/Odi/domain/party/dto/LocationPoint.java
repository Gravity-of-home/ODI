package com.homegravity.Odi.domain.party.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationPoint {

    @Schema(description = "경도 / longitude")
    private double longitude;

    @Schema(description = "위도 / latitude")
    private double latitude;


    @Builder
    private LocationPoint(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public static LocationPoint of(double longitude, double latitude) {
        return builder().longitude(longitude).latitude(latitude).build();
    }

}
