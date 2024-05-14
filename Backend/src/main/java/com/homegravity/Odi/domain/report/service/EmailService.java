package com.homegravity.Odi.domain.report.service;

import com.homegravity.Odi.domain.report.dto.ReportRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${MANAGER_EMAIL}")
    private String to;

    private JavaMailSender javaMailSender;

    public void sendSimpleMessage(ReportRequestDTO requestDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("[유저 신고 내역]");
        message.setText(requestDTO.getContent());
        javaMailSender.send(message);
    }
}
