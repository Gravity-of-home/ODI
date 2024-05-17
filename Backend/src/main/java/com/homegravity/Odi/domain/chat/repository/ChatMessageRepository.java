package com.homegravity.Odi.domain.chat.repository;

import com.homegravity.Odi.domain.chat.entity.ChatMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>{

    Optional<Slice<ChatMessage>> findAllByPartyId(Long partyId, PageRequest pageRequest);

    Optional<ChatMessage> findTopByIdOrderByCreatedAtDesc(Long partyId);

}
