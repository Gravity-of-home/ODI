package com.homegravity.Odi.global.oauth2.service;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.member.repository.MemberRepository;
import com.homegravity.Odi.global.oauth2.dto.CustomOAuth2User;
import com.homegravity.Odi.global.oauth2.dto.MemberDTO;
import com.homegravity.Odi.global.oauth2.dto.NaverResponse;
import com.homegravity.Odi.global.oauth2.dto.OAuth2Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oAuth2User: {}", oAuth2User);


        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("registrationId: {}", registrationId);

        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {

        } else if (registrationId.equals("kakao")) {

        } else {
            oAuth2Response = null;
            return null;
        }

        // 유저 DB 저장
        String providerCode = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        Member existMember = memberRepository.findByProviderCodeAndDeletedAtIsNull(providerCode).orElse(null);
        MemberDTO memberDTO = new MemberDTO();

        if (existMember == null) { // 신규 가입
            existMember = newMember(oAuth2Response, providerCode);

            memberDTO.setProviderCode(providerCode);
            memberDTO.setId(existMember.getId());
            memberDTO.setName(oAuth2Response.getName());
            memberDTO.setRole("ROLE_USER");

        } else {
            existMember.updateEmail(oAuth2Response.getEmail());
            existMember.updateName(oAuth2Response.getName());
            existMember.updateImage(oAuth2Response.getProfileImage());

            memberRepository.save(existMember);

            memberDTO.setProviderCode(existMember.getProviderCode());
            memberDTO.setId(existMember.getId());
            memberDTO.setName(oAuth2Response.getName());
            memberDTO.setRole(existMember.getRole());
        }

        return new CustomOAuth2User(memberDTO);
    }

    private Member newMember(OAuth2Response oAuth2Response, String providerCode) {

        // 신규 가입
        Member member = Member.of(oAuth2Response.getName(), oAuth2Response.getGender(), providerCode, oAuth2Response.getProvider(), oAuth2Response.getEmail(), oAuth2Response.getBirthyear(), oAuth2Response.getBirthday(), oAuth2Response.getNickname(), oAuth2Response.getProfileImage(), "ROLE_USER");
        Member saveMember = memberRepository.save(member);

        return saveMember;
    }
}
