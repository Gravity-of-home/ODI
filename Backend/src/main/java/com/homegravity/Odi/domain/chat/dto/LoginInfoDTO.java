package com.homegravity.Odi.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginInfoDTO {

    private Long memberId;
    private String nickname;
    private String image;
    private String token;

    @Builder
    public LoginInfoDTO(Long memberId, String nickname, String image, String token) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.image = image;
        this.token = token;
    }
}
