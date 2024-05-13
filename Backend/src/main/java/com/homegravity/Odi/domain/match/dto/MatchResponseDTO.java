package com.homegravity.Odi.domain.match.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchResponseDTO {

    @Schema(description = "멤버 id")
    private Long id;

    @Schema(description = "출발지와의 거리 차이")
    private Double depDistance;

    @Schema(description = "출발지명")
    private String depName;

    @Schema(description = "출발지 경도 / longitude")
    private double depLon;

    @Schema(description = "출발지 위도 / latitude")
    private double depLat;

    @Schema(description = "도착지와의 거리 차이")
    private Double arrDistance;

    @Schema(description = "도착지명")
    private String arrName;

    @Schema(description = "도착지 경도 / longitude")
    private double arrLon;

    @Schema(description = "도착지 위도 / latitude")
    private double arrLat;

    @Builder
    private MatchResponseDTO(Long id, Double depDistance, String depName, double depLon, double depLat, Double arrDistance, String arrName, double arrLon, double arrLat) {
        this.id = id;
        this.depDistance = depDistance;
        this.depName = depName;
        this.depLon = depLon;
        this.depLat = depLat;
        this.arrDistance = arrDistance;
        this.arrName = arrName;
        this.arrLon = arrLon;
        this.arrLat = arrLat;
    }
}
