package com.homegravity.Odi.domain.payment.dto.request;

import com.homegravity.Odi.domain.payment.entity.PayType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "결제 요청 DTO")
@Getter
@Setter
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


}
