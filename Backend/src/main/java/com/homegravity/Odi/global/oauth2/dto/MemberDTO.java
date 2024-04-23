package com.homegravity.Odi.global.oauth2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {
    private Long id;
    private String role;
    private String name;
    private String providerCode;
}
