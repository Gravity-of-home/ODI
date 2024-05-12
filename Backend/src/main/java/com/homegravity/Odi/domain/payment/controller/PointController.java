package com.homegravity.Odi.domain.payment.controller;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.payment.dto.response.PointHistoryResponseDto;
import com.homegravity.Odi.domain.payment.service.PointService;
import com.homegravity.Odi.global.response.success.ApiResponse;
import com.homegravity.Odi.global.response.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "포인트", description = "포인트 사용 내역 조회 등")
@RestController
@RequestMapping("/api/point")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @Operation(summary = "포인트 사용 내역 조회", description = "포인트 사용 내역을 조회합니다.(정산 등)")
    @GetMapping("/history")
    public ApiResponse<Slice<PointHistoryResponseDto>> getPointHistory(@AuthenticationPrincipal Member member, Pageable pageable) {

        return ApiResponse.of(SuccessCode.POINT_HISTORY_GET_SUCCESS, pointService.getPointHistory(member, pageable));
    }
}
