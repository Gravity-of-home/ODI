package com.homegravity.Odi.domain.party.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeoPoint {

    private double x;
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
