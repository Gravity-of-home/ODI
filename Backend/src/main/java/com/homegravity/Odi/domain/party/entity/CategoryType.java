package com.homegravity.Odi.domain.party.entity;

public enum CategoryType {
    UNIVERSITY("대학교"),
    DAILY("일상"),
    COMMUTE("출퇴근"),
    CONCERT("콘서트"),
    AIRPORT("공항"),
    TRAVEL("여행"),
    RESERVIST("예비군");

    private final String description;

    CategoryType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
