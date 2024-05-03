package com.homegravity.Odi.domain.member.service;

import com.homegravity.Odi.domain.member.dto.request.MemberUpdateRequestDTO;
import com.homegravity.Odi.domain.member.dto.response.MemberResponseDTO;
import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.member.repository.MemberRepository;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // 사용자 상세정보 조회
    public MemberResponseDTO getMemberInfo(Member member){
        return MemberResponseDTO.from(member);
    }

    //사용자 정보 수정
    public MemberResponseDTO updateMemberInfo(MemberUpdateRequestDTO memberUpdateRequestDTO, Member member){
        boolean IsExistNickname = memberRepository.existsByNicknameAndDeletedAtIsNull(memberUpdateRequestDTO.getNewNickname());

        if(IsExistNickname) {//이미 존재하는 닉네임이면 변경 불가
            throw BusinessException.builder().errorCode(ErrorCode.NICKNAME_ALREAD_EXIST).message(ErrorCode.NICKNAME_ALREAD_EXIST.getMessage()).build();
        }

        //member nickname 업데이트
        member.updateNickname(memberUpdateRequestDTO.getNewNickname());
        memberRepository.save(member);

        return MemberResponseDTO.from(member);
    }
}
