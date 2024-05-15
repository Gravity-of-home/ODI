package com.homegravity.Odi.domain.report.controller;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.report.dto.ReportRequestDTO;
import com.homegravity.Odi.domain.report.service.ReportService;
import com.homegravity.Odi.global.response.success.ApiResponse;
import com.homegravity.Odi.global.response.success.SuccessCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "유저 신고 내역", description = "유저 신고")
@Slf4j
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping()
    public ApiResponse<Void> createReport(@AuthenticationPrincipal Member member, ReportRequestDTO requestDTO, MultipartFile file) {

        /* 신고 생성 로직 호출 */
        reportService.createReport(member, requestDTO);
        return ApiResponse.of(SuccessCode.REPORT_CREATE_SUCCESS);
    }

}
