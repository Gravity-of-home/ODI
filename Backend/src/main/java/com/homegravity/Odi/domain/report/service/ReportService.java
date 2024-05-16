package com.homegravity.Odi.domain.report.service;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.member.repository.MemberRepository;
import com.homegravity.Odi.domain.party.respository.PartyMemberRepository;
import com.homegravity.Odi.domain.party.respository.PartyRepository;
import com.homegravity.Odi.domain.report.dto.ReportRequestDTO;
import com.homegravity.Odi.domain.report.entity.Report;
import com.homegravity.Odi.domain.report.repository.ReportRepository;
import com.homegravity.Odi.global.entity.S3Folder;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import com.homegravity.Odi.global.service.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final PartyRepository partyRepository;
    private final PartyMemberRepository partyMemberRepository;

    private final EmailService emailService;
    private final S3Service s3Service;


    @Transactional
    public void createReport(Member reporter, ReportRequestDTO requestDTO) {

        // 유효성 검사
        if (reporter.getId() == requestDTO.getReportedId()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST_ERROR, "자기 자신을 신고할 수 없습니다.");
        }

        Member reported = memberRepository.findById(requestDTO.getReportedId()).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_ID_NOT_EXIST, ErrorCode.MEMBER_ID_NOT_EXIST.getMessage())); // 신고자

        // 유효성 검사
        // 같이 이용한 적 있는 사용자만 신고할 수 있음
        if (!partyMemberRepository.existMemberInSameParty(requestDTO.getPartyId(), reporter.getId(), requestDTO.getReportedId())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST_ERROR, "함께 택시를 이용한 적 있는 파티원만 신고할 수 있습니다.");
        }

        // 첨부파일 S3 저장
        String imgUrl = "";
        if (requestDTO.getAttachments() != null) {
            imgUrl = s3Service.saveFile(requestDTO.getAttachments(), S3Folder.RECEIPT);
        }

        reportRepository.save(Report.of(requestDTO, imgUrl, reported, reporter));

        // 메일 전송
        emailService.sendReportMessage(requestDTO);
    }
}
