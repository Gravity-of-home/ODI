package com.homegravity.Odi.domain.notification.entity;

import com.homegravity.Odi.domain.member.entity.Member;
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
public class Notification extends BaseBy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;

    @Column(name = "is_read")
    private Boolean isRead;

    @Builder
    private Notification (String content, Member receiver, Party party, NotificationType notificationType, Boolean isRead) {
        this.content = content;
        this.receiver = receiver;
        this.party = party;
        this.notificationType = notificationType;
        this.isRead = isRead;
    }

    public void updateIsRead() {
        this.isRead = true;
    }
}
