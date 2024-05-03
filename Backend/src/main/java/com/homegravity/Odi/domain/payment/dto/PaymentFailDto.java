package com.homegravity.Odi.domain.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "구매자 결제 실패 DTO")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentFailDto {

    @Schema(description = "실패 코드")
    @NotNull
    private String code;

    @Schema(description = "주문 번호")
    @NotNull
    private String orderId;

    @Schema(description = "실패 사유")
    @NotNull
    private String message;
}
