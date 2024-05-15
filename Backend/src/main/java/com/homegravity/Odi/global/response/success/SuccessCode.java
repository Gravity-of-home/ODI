package com.homegravity.Odi.global.response.success;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    /**
     * ******************************* Success CodeList ***************************************
     * common HTTP Status Code
     * 200 : OK                 성공
     * 201 : Created            생성됨
     * 202 : Accepted           허용됨
     * -------------------
     * other HTTP Status Code
     * 204 : No Content         콘텐츠 없음
     * 206 : Partial Content    일부 콘텐츠
     * *********************************************************************************************
     */
    // basic
    INSERT_SUCCESS(201, "삽입 성공"),
    SELECT_SUCCESS(200, "조회 성공"),
    UPDATE_SUCCESS(204, "수정 성공"),
    DELETE_SUCCESS(204, "삭제 성공"),

    /**
     * ******************************* Custom Success CodeList ***************************************
     */
    // Member
    REGISTER_SUCCESS(201, "회원가입에 성공하였습니다."),
    LOGIN_SUCCESS(200, "로그인에 성공하였습니다."),
    LOGOUT_SUCCESS(200, "로그아웃에 성공하였습니다."),
    MEMBER_ID_EXIST(200, "회원 id가 이미 존재합니다."),
    MEMBER_ID_NOT_EXIST(200, "회원 id가 존재하지 않습니다."),
    MEMBER_GET_SUCCESS(200, "회원정보 조회에 성공하였습니다."),
    MEMBER_UPDATE_SUCCESS(200, "회원정보 수정에 성공하였습니다."),
    MEMBER_DELETE_SUCCESS(200, "회원 탈퇴에 성공하였습니다."),
    CHECK_MEMBER_OF_JWT(200, "JWT토큰의 멤버 정보조회에 성공했습니다."),

    // Party
    PARTY_CREATE_SUCCESS(201, "택시 파티 생성에 성공했습니다."),
    PARTY_GET_SUCCESS(200, "택시 파티 조회에 성공했습니다."),
    PARTY_UPDATE_SUCCESS(204, "택시 파티 수정에 성공했습니다."),
    PARTY_DELETE_SUCCESS(204, "택시 파티 삭제에 성공했습니다."),
    PARTY_SUBSCRIBE_SUCCESS(201, "택시 파티 참여 신청에 성공했습니다."),
    PARTY_DELETE_SUBSCRIBE_SUCCESS(204, "택시 파티 참여 신청 취소에 성공했습니다."),
    PARTY_ACCEPT_SUBSCRIBE_SUCCESS(201, "택시 파티 신청자를 참여자로 수락하였습니다."),
    PARTY_REFUSE_SUCCESS(204, "택시 파티 동승 참여 거절에 성공했습니다."),
    PARTY_LIST_GET_SUCCESS(200, "파티 이용내역 조회에 성공했습니다."),


    // Place
    PLACE_LIST_GET_SUCCESS(200, "장소 검색에 성공했습니다."),

    // Map
    PATH_INFO_GET_SUCCESS(200, "경로 정보 조회에 성공했습니다."),

    // Payment
    PAYMENT_HISTORY_GET_SUCCESS(200, "결제 내역 조회에 성공했습니다."),
    PAYMENT_REQUEST_SUCCESS(201, "결제 요청에 성공했습니다."),
    PAYMENT_CONFIRM_SUCCESS(201, "결제 승인에 성공했습니다."),
    PAYMENT_FAIL_UPDATE_SUCCESS(204, "결제 실패 업데이트에 성공했습니다."),

    // Point
    POINT_HISTORY_GET_SUCCESS(200, "포인트 내역 조회에 성공했습니다."),

    // Party Settlement
    PARTY_MATCH_STATE_UPDATE_SUCCESS(204, "동승 성사에 성공했습니다."),
    PARTY_STATE_UPDATE_TO_SETTLING_SUCCESS(204, "동승 정산 요청에 성공했습니다."),
    PARTY_STATE_UPDATE_TO_SETTLEMENT_SUCCESS(204, "동승 정산 요청에 성공했습니다."),
    PARTY_MEMBER_CREATE_BRIX_SUCCESS(201, "동승자 평가 저장에 성공했습니다."),

    // Match
    MATCH_SUCCESS(200, "매칭에 성공했습니다."),
    MATCH_CANCEL_SUCCESS(204, "매칭 취소에 성공했습니다."),

    // Notification
    NOTIFICATION_GET_SUCCESS(200, "알림 목록 조회에 성공했습니다."),
    NOTIFICATION_UPDATE_SUCCESS(204, "알림 읽음 처리에 성공했습니다."),
    NOTIFICATION_DELETE_SUCCESS(204, "알림 삭제에 성공했습니다."),

    // Report
    REPORT_CREATE_SUCCESS(200, "신고 제출에 성공했습니다."),

    ;

    /**
     * ******************************* Success Code Field ***************************************
     */
    // 성공 코드의 '코드 상태'를 반환한다.
    private final int status;

    // 성공 코드의 '코드 메시지'를 반환한다.
    private final String message;
}