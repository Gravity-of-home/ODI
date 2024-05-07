package com.homegravity.Odi.domain.member.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomMemberReviewRepositoryImpl implements CustomMemberReviewRepository{
    private final JPAQueryFactory jpaQueryFactory;

}
