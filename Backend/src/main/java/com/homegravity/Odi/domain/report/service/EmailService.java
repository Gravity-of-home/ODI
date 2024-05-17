package com.homegravity.Odi.domain.report.service;

import com.homegravity.Odi.domain.report.dto.ReportRequestDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${MAIL_USERNAME}")
    private String from;

    @Value("${MANAGER_EMAIL}")
    private String[] to;

    private final JavaMailSender javaMailSender;

    @Async // 비동기 처리
    public void sendReportMessage(ReportRequestDTO requestDTO, String imgUrl) {

        MimeMessage message = javaMailSender.createMimeMessage();

        try {

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String text = "<html>" +
                    "<head>" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; line-height: 1.6; }" +
                    "h3 { color: #333; }" +
                    "p { margin: 5px 0; }" +
                    ".container { width: 100%; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px; background-color: #f9f9f9; }" +
                    ".content { background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                    ".content p { margin: 10px 0; }" +
                    ".content p strong { color: #555; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<div class='content'>" +
                    "<h3>신고 내역</h3>" +
                    "<p><strong>파티 ID:</strong> " + requestDTO.getPartyId() + "</p>" +
                    "<p><strong>채팅방 ID:</strong> " + requestDTO.getRoomId() + "</p>" +
                    "<p><strong>신고 받을 멤버 ID:</strong> " + requestDTO.getReportedId() + "</p>" +
                    "<p><strong>신고 유형:</strong> " + requestDTO.getType().getDescription() + "</p>" +
                    "<p><strong>신고 상세 내용:</strong> " + requestDTO.getContent() + "</p>" +
                    "<div> <p><strong>첨부 파일</p></strong><img src=\"" + imgUrl + "\" width=\"200px\"></div>" +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setFrom(from + "@gmail.com");
            helper.setTo(to);
            helper.setSubject("[유저 신고] 확인부탁드립니다.");
            helper.setText(text, true); // html 사용

            javaMailSender.send(message);

        } catch (MessagingException e) {
            log.error("메일 전송에 실패했습니다.");
            e.printStackTrace();
        }
    }
}
