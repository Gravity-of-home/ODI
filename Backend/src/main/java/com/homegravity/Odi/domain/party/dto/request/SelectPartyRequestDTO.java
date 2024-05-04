package com.homegravity.Odi.domain.party.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.homegravity.Odi.domain.party.dto.LocationPoint;
import com.homegravity.Odi.domain.party.entity.CategoryType;
import com.homegravity.Odi.domain.party.entity.GenderType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;


@Schema(description = "동승 구인 글 조회 DTO")
@Getter
@Setter
@ToString()
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SelectPartyRequestDTO {

    @Schema(description = "오늘 출발 필터링 여부")
    private Boolean isToday;

    @Schema(description = "출발 날짜")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate departuresDate;

    @Schema(description = "성별")
    private GenderType gender;

    @Schema(description = "카테고리")
    private CategoryType category;

    // TODO: 현재 위치 좌표
    @Schema(description = "현재 위치 위도")
    private Double latitude;

    @Schema(description = "현재 위치 경도")
    private Double longitude;

}
