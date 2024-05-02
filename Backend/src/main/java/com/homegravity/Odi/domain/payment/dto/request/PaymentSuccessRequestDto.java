package com.homegravity.Odi.domain.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "구매자 결제 성공 DTO")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentSuccessRequestDto {

    @Schema(description = "결제 키 값")
    @NotNull
    private String paymentKey;

    @Schema(description = "주문 번호")
    @NotNull
    private String orderId;

    @Schema(description = "결제 금액")
    @NotNull
    private Long amount;
}
