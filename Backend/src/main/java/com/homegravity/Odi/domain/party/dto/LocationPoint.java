package com.homegravity.Odi.domain.party.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationPoint {

    @Schema(description = "위도 / latitude")
    private double latitude;
    @Schema(description = "경도 / longitude")
    private double longitude;

    @Builder
    private LocationPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static LocationPoint of(double latitude, double longitude) {
        return builder().latitude(latitude).longitude(longitude).build();
    }

}
