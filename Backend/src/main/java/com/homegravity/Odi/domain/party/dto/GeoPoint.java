package com.homegravity.Odi.domain.party.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeoPoint {

    @Schema(description = "위도 / latitude")
    private double x;
    @Schema(description = "경도 / longitude")
    private double y;

    @Builder
    private GeoPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static GeoPoint of(double x, double y) {
        return builder().x(x).y(y).build();
    }

}
