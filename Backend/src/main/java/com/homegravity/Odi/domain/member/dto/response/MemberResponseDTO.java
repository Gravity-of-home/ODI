package com.homegravity.Odi.domain.member.dto.response;

import com.homegravity.Odi.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "사용자 DTO")
@Getter
@Setter
@NoArgsConstructor
public class MemberResponseDTO {
    @Schema(description = "사용자 아이디(pk)")
    private Long id;

    @Schema(description = "이메일 (사용자 ID)")
    private String email; //이메일

    @Schema(description = "연령대")
    private String ageGroup; //연령대

    @Schema(description = "이름")
    private String name; //이름

    @Schema(description = "닉네임")
    private String nickname; //닉네임

    @Schema(description = "프로필 사진")
    private String image; //프로필 사진

    @Schema(description = "성별")
    private String gender; //성별

    @Schema(description = "매너온도")
    private Double brix; //매너온도

    @Builder
    private MemberResponseDTO(Long id, String email, String ageGroup, String name, String nickname, String image, String gender, Double brix) {
        this.id = id;
        this.email = email;
        this.ageGroup = ageGroup;
        this.name = name;
        this.nickname = nickname;
        this.image = image;
        this.gender = gender;
        this.brix = brix;
    }

    public static MemberResponseDTO from(Member member) {
        return builder()
                .id(member.getId())
                .email(member.getEmail())
                .ageGroup(member.getAgeGroup())
                .name(member.getName())
                .nickname(member.getNickname())
                .image(member.getImage())
                .gender(member.getGender())
                .brix(member.getBrix())
                .build();
    }
}
