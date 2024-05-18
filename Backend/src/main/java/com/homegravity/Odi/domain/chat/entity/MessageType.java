package com.homegravity.Odi.domain.chat.entity;

public enum MessageType {
    ENTER, // 입장
    QUIT, // 퇴장
    KICK, // 강퇴
    TALK, // 채팅
    DATE, // 날짜
    CONFIRM, // 파티성사
    SETTLEMENT_REQUEST, // 정산 요청
    SETTLEMENT_SUCCESS // 정산 완료
}
