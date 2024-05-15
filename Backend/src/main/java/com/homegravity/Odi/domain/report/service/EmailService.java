package com.homegravity.Odi.domain.report.service;

import com.homegravity.Odi.domain.report.dto.ReportRequestDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${MAIL_USERNAME}")
    private String from;

    @Value("${MANAGER_EMAIL}")
    private String to;

    private final JavaMailSender javaMailSender;

    public void sendReportMessage(ReportRequestDTO requestDTO) {

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(from + "@gmail.com");
            helper.setTo(to);
            helper.setSubject("[유저 신고] 확인부탁드립니다.");
            helper.setText(requestDTO.getContent(), true); // html 사용
//
//            if (!requestDTO.getAttachments().isEmpty()) {
//                helper.addAttachment("첨부사진",
//                        new FileSystemResource(new File(requestDTO.getAttachments())));
//            }
            javaMailSender.send(message);
        } catch (MessagingException e) {
            log.error("메일 전송에 실패했습니다.");
            e.printStackTrace();
        }
    }
}
