package com.homegravity.Odi.domain.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.report.dto.ReportRequestDTO;
import com.homegravity.Odi.global.entity.BaseBy;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE report SET deleted_at = NOW() where report_id = ?")
public class Report extends BaseBy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ReportType type;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReportStatus reportStatus;

    @Column(name = "attachments")
    private String attachments;

    @ManyToOne
    @JoinColumn(name = "reported_id", nullable = false) // 신고 받은 멤버
    private Member reportedMember;

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false) // 신고한 멤버
    private Member reporterMember;

    @Column(name = "resolution")
    private String resolution;

    @Column(name = "resolved_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime resolvedAt;

    @Builder
    private Report(ReportType type, String content, ReportStatus reportStatus, String attachments, Member reportedMember, Member reporterMember, String resolution, LocalDateTime resolvedAt) {
        this.type = type;
        this.content = content;
        this.reportStatus = reportStatus;
        this.attachments = attachments;
        this.reportedMember = reportedMember;
        this.reporterMember = reporterMember;
        this.resolution = resolution;
        this.resolvedAt = resolvedAt;
    }

    public static Report of(ReportRequestDTO requestDTO, Member reported, Member reporter) {
        return builder()
                .type(requestDTO.getType())
                .content(requestDTO.getContent())
                .reportStatus(ReportStatus.PENDING)
                .attachments(requestDTO.getAttachments())
                .reportedMember(reported)
                .reporterMember(reporter)
                .build();
    }

    public void updateStatus(ReportStatus status) {
        this.reportStatus = status;
    }

    public void updateResolution(String resolution) {
        this.resolution = resolution;
    }

    public void updateResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }
}
