package com.homegravity.Odi.domain.report.entity;

public enum ReportType {

    NON_PAYMENT("미정산"),
    FRAUD("사기"),
    DANGEROUS_BEHAVIOR("위험한 행동"),
    HARASSMENT("성희롱/괴롭힘"),
    INAPPROPRIATE_LANGUAGE_BEHAVIOR("부적절한 언어/행동"),
    NO_SHOW("노쇼"),
    OTHER("기타");

    private final String description;

    ReportType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
