package com.homegravity.Odi.domain.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.homegravity.Odi.domain.payment.entity.PointHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "포인트 사용 내역 DTO")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistoryResponseDto {

    @Schema(description = "포인트 내역 아이디")
    private Long pointHistoryId;

    @Schema(description = "종류(선불 PREPAYMENT / 정산 SETTLEMENT / 충전 CHARGE)")
    private String type;

    @Schema(description = "연관 파티 아이디(type이 선불 또는 정산일 때)")
    private Long partyId;

    @Schema(description = "연관 결제 아이디(type이 충전일 때)")
    private Long paymentId;

    @Schema(description = "내용")
    private String content;

    @Schema(description = "상세 내용")
    private String detailContent;

    @Schema(description = "금액")
    private Integer amount;

    @Schema(description = "발생 일자")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Builder
    private PointHistoryResponseDto(Long pointHistoryId, String type, Long partyId, Long paymentId, String content, String detailContent, Integer amount, LocalDateTime createdAt) {
        this.pointHistoryId = pointHistoryId;
        this.type = type;
        this.partyId = partyId;
        this.paymentId = paymentId;
        this.content = content;
        this.detailContent = detailContent;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public static PointHistoryResponseDto from(PointHistory pointHistory) {
        return builder()
                .pointHistoryId(pointHistory.getId())
                .type(pointHistory.getType().toString())
                .partyId(pointHistory.getPartyId())
                .paymentId(pointHistory.getPaymentId())
                .content(pointHistory.getContent())
                .detailContent(pointHistory.getDetailContent())
                .amount(pointHistory.getAmount())
                .createdAt(pointHistory.getCreatedAt())
                .build();
    }
}
