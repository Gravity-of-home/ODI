package com.homegravity.Odi.domain.report.dto;

import com.homegravity.Odi.domain.report.entity.ReportType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportRequestDTO {

    @Schema(description = "파티 id")
    private Long partyId;

    @Schema(description = "채팅방 id")
    private String roomId;

    @NotNull
    @Schema(description = "신고 받을 멤버 id")
    private Long reportedId;

    @NotNull
    @Schema(description = "신고 유형")
    private ReportType type;

    @Schema(description = "신고 상세 내용")
    private String content;

    @Schema(description = "첨부파일")
    private String attachments;
}
