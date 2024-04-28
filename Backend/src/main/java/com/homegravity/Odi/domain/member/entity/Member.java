package com.homegravity.Odi.domain.member.entity;


import com.homegravity.Odi.global.entity.BaseTime;
import com.homegravity.Odi.global.oauth2.dto.OAuthProvider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE member SET deleted_at =NOW() WHERE member_id = ?")
public class Member extends BaseTime implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "name")
    private String name;    //실명

    @Column(name = "gender")
    private String gender;  //성별

    @Column(name = "provider_code")
    private String providerCode;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private OAuthProvider provider;

    @Column(name = "email")
    private String email;   //이메일

    @Column(name = "birth")
    private LocalDate birth; //생년월일

    @Column(name = "age_group")
    private String ageGroup; // 나이대

    @Column(name = "nickname")
    private String nickname;   //  닉네임

    @Column(name = "point")
    private int point;  //포인트

    @Column(name = "profile_img")
    private String image;   //프로필 사진

    @Column(name = "role")
    private String role;

    @Column(name = "is_verifired")
    private boolean isVerifired;    //실명인증확인여부

    @Column(name = "brix")
    private Double brix;    //매너온도

    @Builder
    private Member(String name, String gender, String providerCode, OAuthProvider provider, String email, LocalDate birth, String ageGroup, String nickname, int point, String image, String role, boolean isVerifired, double brix) {
        this.name = name;
        this.gender = gender;
        this.providerCode = providerCode;
        this.provider = provider;
        this.email = email;
        this.birth = birth;
        this.ageGroup = ageGroup;
        this.nickname = nickname;
        this.point = point;
        this.image = image;
        this.role = role;
        this.isVerifired = isVerifired;
        this.brix = brix;
    }

    //사용자 정보 저장 시 사용
    public static Member of(String name, String gender, String providerCode, OAuthProvider provider, String email, String birthyear, String birthday, String nickname, String image, String role) {

        int age = ((LocalDate.now().getYear() - Integer.parseInt(birthyear)) / 10) * 10;

        return builder()
                .name(name)
                .providerCode(providerCode)
                .email(email)
                .image(image)
                .gender(gender)
                .role(role)
                .provider(provider)
                .isVerifired(false)
                .nickname(nickname)
                .birth(LocalDate.parse(birthyear + "-" + birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .ageGroup(age + "대")
                .build();
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateImage(String image) {
        this.image = image;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return Long.toString(id);
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
