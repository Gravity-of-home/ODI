package com.homegravity.Odi.domain.payment.service;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.member.repository.MemberRepository;
import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.payment.entity.PointHistory;
import com.homegravity.Odi.domain.payment.entity.PointHistoryType;
import com.homegravity.Odi.domain.payment.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PointService {

    private final MemberRepository memberRepository;
    private final PointHistoryRepository pointHistoryRepository;

    private final String POINT_HISTORY_DETAIL_CONTENT = "%s -> %s (%d원, %d명 동승)";

    // 파티 선불/정산 및 내역 생성
    public void usePoint(Member member, Party party, int wholeCost, int memberCost, int paidAmount) {

        // 포인트 사용
        member.updatePoint(paidAmount-memberCost);
        memberRepository.save(member);

        // 포인트 사용 내역 생성
        String detailContent = String.format(POINT_HISTORY_DETAIL_CONTENT, party.getDeparturesName(), party.getArrivalsName(), wholeCost, party.getCurrentParticipants());
        pointHistoryRepository.save(PointHistory.createSettleHistory(member, party.getId(), party.getTitle(), detailContent, PointHistoryType.PREPAYMENT, memberCost));
    }
}
