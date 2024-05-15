package com.homegravity.Odi.domain.report.service;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.member.repository.MemberRepository;
import com.homegravity.Odi.domain.report.dto.ReportRequestDTO;
import com.homegravity.Odi.domain.report.entity.Report;
import com.homegravity.Odi.domain.report.repository.ReportRepository;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;

    private final EmailService emailService;

    @Transactional
    public void createReport(Member reporter, ReportRequestDTO requestDTO) {

        Member reported = memberRepository.findById(requestDTO.getReportedId()).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_ID_NOT_EXIST, ErrorCode.MEMBER_ID_NOT_EXIST.getMessage())); // 신고자
        reportRepository.save(Report.of(requestDTO, reported, reporter));

        // 메일 전송
        emailService.sendReportMessage(requestDTO);
    }
}
