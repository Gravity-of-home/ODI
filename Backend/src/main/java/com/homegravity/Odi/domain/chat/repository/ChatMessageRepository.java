package com.homegravity.Odi.domain.chat.repository;

import com.homegravity.Odi.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>{

    Optional<List<ChatMessage>> findAllByPartyId(Long partyId);

    Optional<ChatMessage> findTopByIdOrderByCreatedAtDesc(Long partyId);
}
