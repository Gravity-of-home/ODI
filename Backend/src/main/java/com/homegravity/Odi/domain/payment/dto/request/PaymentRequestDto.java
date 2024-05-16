package com.homegravity.Odi.domain.payment.dto.request;

import com.homegravity.Odi.domain.payment.entity.PayType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "결제 요청 DTO")
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentRequestDto {

    @Schema(description = "결제 타입 (카드/현금/포인트)")
    @NotNull
    private PayType payType;

    @Schema(description = "결제 금액")
    @NotNull
    private Integer amount;

    @Schema(description = "주문 내용(구매상품)")
    @NotNull
    private String orderName;

    @Schema(description = "생성일자(Default 있음)")
    private LocalDateTime requestedAt = LocalDateTime.now();

}
