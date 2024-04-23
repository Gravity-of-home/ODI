package com.homegravity.Odi.domain.member.entity;


import com.homegravity.Odi.global.entity.BaseTime;
import com.homegravity.Odi.global.oauth2.dto.OAuthProvider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private Date birth; //생년월일

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
    private double brix;    //매너온도

    @Builder
    public Member(Long id, String name, String gender, String providerCode, OAuthProvider provider, String email, Date birth, String nickname, int point, String image, String role, boolean isVerifired, double brix) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.providerCode = providerCode;
        this.provider = provider;
        this.email = email;
        this.birth = birth;
        this.nickname = nickname;
        this.point = point;
        this.image = image;
        this.role = role;
        this.isVerifired = isVerifired;
        this.brix = brix;
    }


    //사용자 정보 저장 시 사용
    public static Member of(String name, String gender, String providerCode, OAuthProvider provider, String email, String birthyear, String birthday, String nickname, String image, String role) {
        try {
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
                    .birth(new SimpleDateFormat("yyyy-MM-dd").parse(birthyear + "-" + birthday))
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
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
