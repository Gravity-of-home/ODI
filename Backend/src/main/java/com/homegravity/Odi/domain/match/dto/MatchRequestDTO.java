package com.homegravity.Odi.domain.match.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "매칭 요청")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchRequestDTO {

    @Schema(description = "출발지명")
    private String depName;

    @Schema(description = "출발지 경도 / longitude")
    private double depLon;

    @Schema(description = "출발지 위도 / latitude")
    private double depLat;

    @Schema(description = "도착지명")
    private String arrName;

    @Schema(description = "도착지 경도 / longitude")
    private double arrLon;

    @Schema(description = "도착지 위도 / latitude")
    private double arrLat;

}
