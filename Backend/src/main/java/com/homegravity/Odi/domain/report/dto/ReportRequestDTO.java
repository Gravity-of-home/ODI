package com.homegravity.Odi.domain.report.dto;

import com.homegravity.Odi.domain.report.entity.ReportType;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportRequestDTO {

    private Long partyId;

    private String roomId;

    @NotNull
    private Long reportedId;

    @NotNull
    private ReportType type;

    private String content;

    private String attachments;
}
