package com.homegravity.Odi.domain.payment.dto.toss;

public enum PaymentConfirmationStatus {

    READY, // 결제 생성 시 가지는 초기 상태 (인증 전까지의 상태)
    IN_PROGRESS, // 결제수단 정보와 해당 결제수단의 소유자가 맞는지 인증을 마친 상태 - 결제 승인 API 호출로 결제 완료 가능
    WAITING_FOR_DEPOSIT, // 가상계좌 입금 기다리는 상태
    DONE, // 결제가 승인된 상태
    CANCELED, // 승인된 결제가 취소된 상태
    PARTIAL_CANCELED, // 승인된 결제가 부분 취소된 상태
    ABORTED, // 결제 승인이 실패
    EXPIRED // 결제 유효시간(30분)이 지나 거래가 취소됨

}
