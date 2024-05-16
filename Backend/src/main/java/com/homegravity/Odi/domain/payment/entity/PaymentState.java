package com.homegravity.Odi.domain.payment.entity;

public enum PaymentState {

    NOT_STARTED, // 결제 승인 시작 전
    EXECUTING, // 결제 승인 중 : 사용자의 인증 완료로 결제 승인으로 넘어간 상태
    SUCCESS, // 결제 승인 성공
    FAILURE, // 결제 승인 실패
    UNKNOWN // 결제가 알 수 없는 상태
}
