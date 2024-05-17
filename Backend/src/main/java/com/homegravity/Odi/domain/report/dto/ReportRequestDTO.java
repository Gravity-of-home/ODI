package com.homegravity.Odi.domain.report.dto;

import com.homegravity.Odi.domain.report.entity.ReportType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportRequestDTO {

    @NotNull
    @Schema(description = "파티 id")
    private Long partyId;

    @NotNull
    @Schema(description = "신고 받을 멤버 id")
    private Long reportedId;

    @NotNull
    @Schema(description = "신고 유형")
    private ReportType type;

    @Schema(description = "신고 상세 내용")
    private String content;

    @Schema(description = "첨부파일")
    private MultipartFile attachments;
}
