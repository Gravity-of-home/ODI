package com.homegravity.Odi.domain.chat.entity;

import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.global.entity.BaseBy;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE chat_message SET deleted_at = NOW() where chat_message_id = ?")
public class ChatMessage extends BaseBy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "sender_id")
    private Long senderId;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    private MessageType messageType;

    @Builder
    private ChatMessage(MessageType messageType) {

    }
}
